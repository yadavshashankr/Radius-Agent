package com.shashank.radiusAgent.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class OptionsModel(var name : String? = null, var icon : String? = null, var id : String? = null, var selectedFacility: String? = null, var isEnabled : Boolean = true){
    object Converters {
        @TypeConverter
        fun fromString(value : String?) : ArrayList<OptionsModel> {
            val listType = object : TypeToken<ArrayList<OptionsModel?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list : ArrayList<OptionsModel?>?) : String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}