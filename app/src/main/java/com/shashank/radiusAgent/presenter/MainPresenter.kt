package com.shashank.radiusAgent.presenter

import android.util.Log
import com.shashank.radiusAgent.utils.launchOnMain
import com.shashank.radiusAgent.network.model.FacilityModel
import com.shashank.radiusAgent.contracts.MainActivityContract
import com.shashank.radiusAgent.network.model.ExclusionModel
import com.shashank.radiusAgent.network.model.MainModel
import com.shashank.radiusAgent.utils.Prefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainPresenter(
    private val view : MainActivityContract.View,
    private val model : MainActivityContract.Model
) : MainActivityContract.Presenter, MainActivityContract.Model.OnFinishListener,
    MainActivityContract.OptionsProcessor {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getData() {
        scope.launch {
            if (isMoreThan24Hours()) model.getApiData(onFinishListener = this@MainPresenter) else getDBData()
        }
    }

    private fun isMoreThan24Hours() : Boolean {
        val lastRecordedTime = Prefs.getLastRecordedTime()
        return lastRecordedTime == 0L || System.currentTimeMillis() >= lastRecordedTime + 24 * 60 * 60 * 1000
    }

    override fun onLoading() {
        scope.launchOnMain { view.onLoading() }
    }

    override suspend fun onSuccess() {
        scope.launch { view.onSuccess(model.getDBData() as MainModel) }
    }

    override fun onError(message : String) {
        Log.e("ERR", message)
        scope.launch { getDBData() }
    }

    override fun onDestroy() {
        scope.cancel()
    }

    private suspend fun getDBData() {
        scope.launch {
            val dbData = model.getDBData()
            if (dbData != null) {
                view.onSuccess(dbData)
            } else {
                view.onError("Internet not available!")
            }
        }
    }

    override fun disableSelectedOptionsState(
        facilitiesList : ArrayList<FacilityModel>,
        valueFacilityId : String,
        valueOptionsId : String
    ) : ArrayList<FacilityModel> {
        var i = 0
        var j = 0

        while (i < facilitiesList.size) {
            while (j < facilitiesList[i].options?.size as Int) {
                facilitiesList[i].options?.get(j)?.isEnabled =
                    if (valueFacilityId == "" || valueOptionsId == "") true
                    else valueFacilityId != facilitiesList[i].facility_id as String || valueOptionsId != facilitiesList[i].options?.get(
                        j
                    )?.id as String

                j++
            }
            j = 0
            i++
        }
        return facilitiesList
    }

    override fun processOptions(model : MainModel) : ArrayList<FacilityModel> {
        val exclusionList = model.exclusions
        val facilitiesList = model.facilities
        val hashMap: HashMap<String, String> = HashMap()

        return allotExcludedIdsToOptions(
            enableAllOptions(facilitiesList as ArrayList<FacilityModel>),
            exclusionList as ArrayList<ArrayList<ExclusionModel>>,
            hashMap
        )
    }

    private fun allotExcludedIdsToOptions(
        facilitiesList : ArrayList<FacilityModel>,
        exclusionList : ArrayList<ArrayList<ExclusionModel>>,
        hashMap : HashMap<String, String>
    ) : ArrayList<FacilityModel> {
        var i = 0
        var j = 0

        while (i < facilitiesList.size) {
            var value = ""
            var keyValue = ""
            while (j < exclusionList[i].size) {
                val rejectedFacilityId = exclusionList[i][j].facility_id as String
                val rejectedOptionId = exclusionList[i][j].options_id as String

                if (j == exclusionList[j].size - 1) {
                    value += "$rejectedFacilityId-$rejectedOptionId"
                } else {
                    keyValue = "$rejectedFacilityId-$rejectedOptionId"
                }
                hashMap[keyValue] = value
                j++
            }
            j = 0
            i++
        }

        i = 0
        j = 0

        while (i < facilitiesList.size) {
            while (j < facilitiesList[i].options?.size as Int) {
                val value =
                    "${facilitiesList[i].facility_id}-${facilitiesList[i].options?.get(j)?.id}"
                facilitiesList[i].options?.get(j)?.selectedFacility =
                    hashMap.getOrDefault(value, "")
                j++
            }
            j = 0
            i++
        }
        return facilitiesList
    }

    private fun enableAllOptions(facilitiesList : ArrayList<FacilityModel>) : ArrayList<FacilityModel> {
        var i = 0
        var j = 0
        while (i < facilitiesList.size) {
            while (j < facilitiesList[i].options?.size as Int) {
                facilitiesList[i].options?.get(j)?.isEnabled = true
                j++
            }
            j = 0
            i++
        }
        return facilitiesList
    }
}