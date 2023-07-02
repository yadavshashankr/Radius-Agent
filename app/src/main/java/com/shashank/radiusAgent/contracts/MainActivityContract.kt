package com.shashank.radiusAgent.contracts


import com.shashank.radiusAgent.network.model.FacilityModel
import com.shashank.radiusAgent.network.model.MainModel

interface MainActivityContract {

    interface View{
        fun onLoading()
        fun onSuccess(model : MainModel?)
        fun onError(message :String?)
    }

    interface Presenter{
        fun getData()
        fun onDestroy()
    }

    interface Model{
        interface OnFinishListener{
            fun onLoading()
            suspend fun onError(message:String)
            suspend fun onSuccess()
        }
        suspend fun getApiData(onFinishListener : OnFinishListener)
        suspend fun getDBData() : MainModel?
    }

    interface OptionsProcessor{
        fun disableSelectedOptionsState(facilitiesList : ArrayList<FacilityModel>, valueFacilityId : String, valueOptionsId : String) : ArrayList<FacilityModel>
        fun processOptions(model : MainModel): ArrayList<FacilityModel>

        fun getFinalPosition(selectedPosition : Int) : Int
    }
}