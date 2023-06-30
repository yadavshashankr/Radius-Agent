package com.shashank.radiusAgent.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shashank.radiusAgent.globals.Constants
import com.shashank.radiusAgent.network.model.MainModel


@Dao
interface Dao {
    @Query("SELECT * FROM ${Constants.TABLE_NAME_PROPERTY}")
    fun getAllProperties(): MainModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecords(model: MainModel)

    @Query("DELETE FROM ${Constants.TABLE_NAME_PROPERTY}" )
    fun deleteAllRecords()
}