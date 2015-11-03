package com.dream.makermb.utils;

import android.content.Context;
import android.text.TextUtils;

import com.dream.makermb.constant.Debug;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

public class UMSUtil {

    public static void initialize(Context context) {
        MobclickAgent.setDebugMode(Debug.DEV);
        MobclickAgent.updateOnlineConfig(context);
        MobclickAgent.setCatchUncaughtExceptions(!Debug.DEV);
        /*
        * MobclickAgent.openActivityDurationTrack(false)作用：
        * 禁止友盟通过MobclickAgent.onResume(this)以及MobclickAgent.onPause(this)
        * 方法自动统计Activity页面PV
        * */
        MobclickAgent.openActivityDurationTrack(false);
    }

    public static void beginStatPV(Context ctx, String statPVName, HashMap<String, String> statPVParam, boolean isFromActivity) {
        if (!android.text.TextUtils.isEmpty(statPVName)) {
            MobclickAgent.onPageStart(statPVName);
        }
        if (isFromActivity) {
            MobclickAgent.onResume(ctx);
        }
    }

    public static void endStatPV(Context ctx, String statPVName, boolean isFromActivity) {
        if (!TextUtils.isEmpty(statPVName)) {
            MobclickAgent.onPageEnd(statPVName);
        }
        if (isFromActivity) {
            MobclickAgent.onPause(ctx);
        }
    }

    public static void postStatEvent(Context ctx, String eventId, HashMap<String, String> eventParams) {
        if (eventParams == null || eventParams.isEmpty()) {
            MobclickAgent.onEvent(ctx, eventId);
        } else {
            MobclickAgent.onEvent(ctx, eventId, eventParams);
        }
    }
}
