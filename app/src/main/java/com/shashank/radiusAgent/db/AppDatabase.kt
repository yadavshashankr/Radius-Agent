package com.shashank.radiusAgent.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shashank.radiusAgent.network.model.ExclusionModel
import com.shashank.radiusAgent.network.model.FacilityModel
import com.shashank.radiusAgent.network.model.MainModel
import com.shashank.radiusAgent.network.model.OptionsModel


@Database(entities = [MainModel::class],
                version = 1,
                exportSchema = false)
@TypeConverters(
    MainModel.ConvertersFacilities::class,
    MainModel.ConvertersExclusions::class,
    ExclusionModel.TypeConverterData::class,
    OptionsModel.Converters::class,
    FacilityModel.Converters::class)

abstract class AppDatabase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object{
        private var DB_INSTANCE: AppDatabase? = null

        fun getAppDBInstance(context: Context): AppDatabase {
            if(DB_INSTANCE == null) {
                DB_INSTANCE =  Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "APP_DB")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return DB_INSTANCE!!
        }
    }
}
