package com.dream.makermb.setting;

import android.content.Context;

import com.dream.makermb.CustomApp;

public class NormalSetting extends BaseSetting {

    private static NormalSetting mInstance;

    private NormalSetting(Context context) {
        super(context, "normal");
    }

    public static NormalSetting getInstance() {
        if (mInstance == null) {
            mInstance = new NormalSetting(CustomApp.get());
        }
        return mInstance;
    }
}
