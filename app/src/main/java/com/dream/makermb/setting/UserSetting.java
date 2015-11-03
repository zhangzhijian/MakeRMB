package com.dream.makermb.setting;

import android.content.Context;

import com.dream.makermb.CustomApp;

public class UserSetting extends BaseSetting {

    private static final String USER_NAME = "USER_NAME";

    private static UserSetting mInstance;

    private UserSetting(Context context) {
        super(context, "user");
    }

    public static UserSetting getInstance() {
        if (mInstance == null) {
            mInstance = new UserSetting(CustomApp.get());
        }
        return mInstance;
    }

    public String getUserName() {
        return getString(USER_NAME, null);
    }

    public void setUserName(String userName) {
        putString(USER_NAME, String.valueOf(userName));
    }
}
