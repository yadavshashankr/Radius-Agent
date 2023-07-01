package com.shashank.radiusAgent.network.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shashank.radiusAgent.globals.Constants

@Entity(tableName = Constants.TABLE_NAME_PROPERTY)
data class MainModel(@PrimaryKey(autoGenerate = true)@ColumnInfo(name = "id")val id: Int = 0,
                     @ColumnInfo(name = "facilities")var facilities : ArrayList<FacilityModel>?, @ColumnInfo(name = "exclusions")var exclusions : ArrayList<ArrayList<ExclusionModel>>?){
    object ConvertersFacilities {
        @TypeConverter
        fun fromString(value: String?) : ArrayList<String> {
            val listType = object : TypeToken<ArrayList<FacilityModel?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list : ArrayList<String?>?): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }

    object ConvertersExclusions {
        @TypeConverter
        fun fromString(value: String?) : ArrayList<ArrayList<ExclusionModel>> {
            val listType = object : TypeToken<ArrayList<ArrayList<ExclusionModel>?>?>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromArrayList(list : ArrayList<ArrayList<ExclusionModel>?>?) : String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}

