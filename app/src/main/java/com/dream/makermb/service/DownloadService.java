package com.dream.makermb.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.dream.makermb.constant.Dir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 1. 暂不支持多任务同时下载，只能任务排队下载,<br\>
 * 2. 已LocalBroadcastManager广播方式发送下载进度，广播action为下载url<br\>
 * 3. 暂不支持中断下载
 * 4. 待优化：mUrlList在Intent中传递
 */
public class DownloadService extends IntentService {
    private static final String EXTRA_KEY_URL = "EXTRA_KEY_URL";
    private static final int TIMEOUT = 3000;
    private List<String> mUrlList;
    private long mNotifyTime;

    public DownloadService() {
        super("download");
        mUrlList = new ArrayList<>();
    }

    public static Intent createIntent(Context ctx, String url) {
        Intent i = new Intent(ctx, DownloadService.class);
        i.putExtra(EXTRA_KEY_URL, url);
        return i;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String url = intent.getStringExtra(EXTRA_KEY_URL);
        mUrlList.add(url);
        if (mUrlList.size() == 1) {
            startDownload();
        }
    }

    private void downloading(int totalSize, int downloaded) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - mNotifyTime > 1000) {
            mNotifyTime = currentTime;
            Intent i = new Intent();
            i.setAction(mUrlList.get(0));
            i.putExtra("status", 0);
            i.putExtra("total", totalSize);
            i.putExtra("downloaded", downloaded);
            LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        }
    }

    private void downloadSuccess(int totalSize) {
        Intent i = new Intent();
        i.setAction(mUrlList.get(0));
        i.putExtra("status", 1);
        i.putExtra("total", totalSize);
        i.putExtra("path", Dir.FILE_DIR + URLEncoder.encode(mUrlList.get(0)));
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        mUrlList.remove(0);
        if (mUrlList.size() > 0) {
            startDownload();
        }
    }

    private void downloadFailure() {
        Intent i = new Intent();
        i.setAction(mUrlList.get(0));
        i.putExtra("status", 2);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        mUrlList.remove(0);
        if (mUrlList.size() > 0) {
            startDownload();
        }
    }

    private synchronized void startDownload() {
        if (mUrlList.size() <= 0) {
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File updateFile = new File(Dir.FILE_DIR + URLEncoder.encode(mUrlList.get(0)));
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(mUrlList.get(0));
            httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setReadTimeout(5 * TIMEOUT);
            // 获取下载文件的size
            int totalSize = httpURLConnection.getContentLength();
            int ret_code = httpURLConnection.getResponseCode();
            if (ret_code == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                outputStream = new FileOutputStream(updateFile, false);// 文件存在则覆盖掉
                byte buffer[] = new byte[512];
                int readSize = 0;
                int downloadCount = 0;
                while ((readSize = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readSize);
                    downloadCount += readSize;// 实时获取下载到的大小
                    downloading(totalSize, downloadCount);
                }
                downloadSuccess(totalSize);
            } else {
                downloadFailure();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (updateFile.exists()) {
                updateFile.delete();
            }
            downloadFailure();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (mUrlList.size() > 0) {
                startDownload();
            }
        }
    }
}
