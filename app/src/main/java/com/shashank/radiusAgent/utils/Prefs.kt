package com.shashank.radiusAgent.utils

import android.content.Context
import android.content.SharedPreferences
import com.shashank.radiusAgent.R

class Prefs {
    companion object{
        private const val RECORD_TIME_STAMP = "recordTimeStamp"

        private lateinit var sharedPreferences : SharedPreferences

        fun init(context : Context) {
            sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        }

        fun getLastRecordedTime() : Long {
            return sharedPreferences.getLong(RECORD_TIME_STAMP, 0L)
        }

        fun setLastRecordedTime(value : Long) {
            sharedPreferences.edit().putLong(RECORD_TIME_STAMP, value).apply()
        }
    }

}