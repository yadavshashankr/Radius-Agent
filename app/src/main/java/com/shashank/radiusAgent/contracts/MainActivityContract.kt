package com.shashank.radiusAgent.contracts


import com.shashank.radiusAgent.network.model.FacilityModel
import com.shashank.radiusAgent.network.model.MainModel

interface MainActivityContract {

    interface View{
        fun onLoading()
        suspend fun onSuccess(model: MainModel)
        fun onError(message:String)
    }

    interface Presenter{
        fun getData(fromApi : Boolean)
        fun onDestroy()
    }


    interface Model{
        interface OnFinishListener{
            fun onLoading()
            fun onError(message:String)
            suspend fun onSuccess()
        }
        suspend fun fetchModel(onFinishListener: OnFinishListener)
        suspend fun getData() : MainModel
    }

    interface OptionsProcessor{
        fun disableSelectedOptionsState(facilitiesList: ArrayList<FacilityModel>, valueFacilityId : String, valueOptionsId : String) : ArrayList<FacilityModel>
        fun processOptions(model: MainModel): ArrayList<FacilityModel>
    }
}