package com.dream.makermb.setting;

import android.content.Context;
import android.content.SharedPreferences;

public class BaseSetting {
	protected SharedPreferences mPreference;
	protected Context mContext;
	
	public BaseSetting(Context context, String name) {
		mContext = context;
		mPreference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	protected void putString(String key, String value) {
		mPreference.edit().putString(key, value).commit();
	}

	protected void putLong(String key, long value) {
		mPreference.edit().putLong(key, value).commit();
	}

	protected void putInt(String key, int value) {
		mPreference.edit().putInt(key, value).commit();
	}

	protected void putBoolean(String key, boolean value) {
		mPreference.edit().putBoolean(key, value).commit();
	}

	protected long getLong(String key, long defValue) {
		return mPreference.getLong(key, defValue);
	}

	protected String getString(String key, String defValue) {
		return mPreference.getString(key, defValue);
	}

	protected int getInt(String key, int defValue) {
		return mPreference.getInt(key, defValue);
	}

	protected boolean getBoolean(String key, boolean defValue) {
		return mPreference.getBoolean(key, defValue);
	}
}
