package com.dream.makermb.utils;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {

    public static void start(Context context, Intent intent) {
        if (context != null && intent != null) {
            context.startActivity(intent);
        }
    }

    public static void start(Context context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
    }

}
