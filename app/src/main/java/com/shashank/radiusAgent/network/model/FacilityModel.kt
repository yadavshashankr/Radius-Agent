package com.shashank.radiusAgent.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


data class FacilityModel(var facility_id : String? = null, var name : String? = null, var options : ArrayList<OptionsModel>?){
    object Converters {
        @TypeConverter
        fun fromString(value : String?): ArrayList<FacilityModel> {
            val listType = object : TypeToken<ArrayList<FacilityModel?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list: ArrayList<FacilityModel?>?) : String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}