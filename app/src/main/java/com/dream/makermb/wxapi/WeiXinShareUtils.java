package com.dream.makermb.wxapi;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.dream.makermb.CustomApp;
import com.dream.makermb.R;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class WeiXinShareUtils {

    private static final int THUMB_SIZE = 150;

    public static void sendShare(final boolean isFriend, final String iconUrl, final String url, final String title, final String desc, final Resources res) {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = null;
                try {
                    if (null != res && (null == iconUrl || "".equals(iconUrl))) {
                        bmp = BitmapFactory.decodeResource(res, R.drawable.share_icon_default);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                IWXAPI iwxAPI = CustomApp.get().getWXApi();
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = url;

                WXMediaMessage wxMediaMessage = new WXMediaMessage(webpage);
                if (null != iconUrl && !"".equals(iconUrl)) {
                    try {
                        bmp = BitmapFactory.decodeStream(new URL(iconUrl).openStream());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (bmp != null) {
                    Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                    bmp.recycle();
                    wxMediaMessage.thumbData = bmpToByteArray(thumbBmp, true);
                }
                wxMediaMessage.title = title;
                wxMediaMessage.description = desc;

                req.transaction = "" + System.currentTimeMillis();
                req.message = wxMediaMessage;
                req.scene = isFriend ? SendMessageToWX.Req.WXSceneSession
                        : SendMessageToWX.Req.WXSceneTimeline;
                iwxAPI.sendReq(req);
            }

        });

        thread.start();

    }


    public static byte[] bmpToByteArray(final Bitmap bmp,
                                        final boolean needRecycle) {
        Bitmap temp = Bitmap.createBitmap(THUMB_SIZE, THUMB_SIZE,
                Config.RGB_565);
        Canvas canvas = new Canvas(temp);

        int right, bottom;
        if (bmp.getHeight() > bmp.getWidth()) {
            right = bmp.getWidth();
            bottom = bmp.getWidth();
        } else {
            right = bmp.getHeight();
            bottom = bmp.getHeight();
        }
        Rect src = new Rect(0, 0, right, bottom);
        canvas.drawBitmap(bmp, src, new Rect(0, 0, THUMB_SIZE, THUMB_SIZE),
                null);
        if (needRecycle) {
            bmp.recycle();
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        temp.compress(CompressFormat.PNG, 100, output);
        temp.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
