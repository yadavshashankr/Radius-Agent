package com.shashank.radiusAgent.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.shashank.radiusAgent.R;

public class Prefs {
    private static final String RECORD_TIME_STAMP = "r";

    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public static Long getLastRecordedTime() {
        return sharedPreferences.getLong(RECORD_TIME_STAMP, 0L);
    }

    public static void setLastRecordedTime(long value) {
        sharedPreferences.edit().putLong(RECORD_TIME_STAMP, value).apply();
    }

}