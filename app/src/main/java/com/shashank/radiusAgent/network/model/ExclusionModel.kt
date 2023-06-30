package com.shashank.radiusAgent.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

data class ExclusionModel(var facility_id : String? = null, var options_id : String? = null){
    class TypeConverterData {
        private val gson : Gson = Gson()
        @TypeConverter
        fun stringToSomeObjectList(data: String?) : ExclusionModel? {
            if(data == null)return null

            val listType: Type = object : TypeToken<ExclusionModel>() {}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        fun someObjectListToString(someObject: ExclusionModel?): String?
        {
            return gson.toJson(someObject)
        }
    }
}