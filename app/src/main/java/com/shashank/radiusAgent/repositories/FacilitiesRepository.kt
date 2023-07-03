package com.shashank.radiusAgent.repositories

import com.shashank.radiusAgent.contracts.MainActivityContract
import com.shashank.radiusAgent.db.Dao
import com.shashank.radiusAgent.network.api.ApiService
import com.shashank.radiusAgent.network.model.MainModel
import com.shashank.radiusAgent.utils.Prefs
import java.lang.Exception
import javax.inject.Inject

class FacilitiesRepository @Inject constructor(private val apiService : ApiService, private  val dao : Dao) : MainActivityContract.Model {

    override suspend fun getApiData(onFinishListener : MainActivityContract.Model.OnFinishListener) {
        onFinishListener.onLoading()
        try{
            val response = apiService.getCategory()

            if(response.isSuccessful){
                response.body()?.let {
                    dao.insertRecords(it)
                    Prefs.setLastRecordedTime(System.currentTimeMillis())
                    onFinishListener.onSuccess()
                }
            }else{
                onFinishListener.onError(response.errorBody().toString())
            }
        }catch (e : Exception){
            onFinishListener.onError(message = e.message.toString())
        }
    }

    override suspend fun getDBData() : MainModel {
        return dao.getAllFacilities()
    }
}