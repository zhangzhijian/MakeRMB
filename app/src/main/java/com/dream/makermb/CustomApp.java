package com.dream.makermb;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.dream.makermb.wxapi.WeiXinShareConstants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.List;

public class CustomApp extends Application {
    private static CustomApp mInstance;
    private IWXAPI mWXApi;

    public static CustomApp get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String curProcessName = getCurProcessName();
        String mainProcessName = getPackageName();
        if (curProcessName != null && !curProcessName.equals(mainProcessName)) {
            return;
        }

        SDKInitializer.initialize(getApplicationContext());
        mWXApi = WXAPIFactory.createWXAPI(getApplicationContext(), WeiXinShareConstants.AppID, true);
        mWXApi.registerApp(WeiXinShareConstants.AppID);

        mInstance = this;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    private String getCurProcessName() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> allProcesses = activityManager.getRunningAppProcesses();
            if (allProcesses != null) {
                for (ActivityManager.RunningAppProcessInfo process : allProcesses) {
                    if (process != null && process.pid == pid) {
                        return process.processName;
                    }
                }
            }
        }
        return null;
    }
}
