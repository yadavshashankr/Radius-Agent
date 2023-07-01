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

    private suspend fun getDBData() {
        val dbData = model.getDBData()
        if (dbData != null) {
            view.onSuccess(dbData)
        } else {
            view.onError("Internet not available!")
        }
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

    override fun disableSelectedOptionsState(
        facilitiesList : ArrayList<FacilityModel>,
        valueFacilityId : String,
        valueOptionsId : String
    ) : ArrayList<FacilityModel> {
        var facilityPosition = 0
        var optionPosition = 0

        //If mapped facility id and option id is found then disable it or enable the ones whose id is not found.

        while (facilityPosition < facilitiesList.size) {
            while (optionPosition < facilitiesList[facilityPosition].options?.size as Int) {
                facilitiesList[facilityPosition].options?.get(optionPosition)?.isEnabled =
                    if (valueFacilityId == "" || valueOptionsId == "")
                        true
                    else
                        valueFacilityId != facilitiesList[facilityPosition].facility_id as String || valueOptionsId != facilitiesList[facilityPosition].options?.get(optionPosition)?.id as String

                optionPosition++
            }
            optionPosition = 0
            facilityPosition++
        }
        return facilitiesList
    }

    override fun processOptions(model : MainModel) : ArrayList<FacilityModel> {
        val exclusionList = model.exclusions
        val facilitiesList = model.facilities

        return allotExcludedIdsToOptions(facilitiesList as ArrayList<FacilityModel>, exclusionList as ArrayList<ArrayList<ExclusionModel>>
        )
    }

    private fun allotExcludedIdsToOptions(
        facilitiesList : ArrayList<FacilityModel>,
        exclusionList : ArrayList<ArrayList<ExclusionModel>>
    ) : ArrayList<FacilityModel> {
        var facilityPosition = 0
        var optionPosition = 0
        val hashMap: HashMap<String, String> = HashMap()

        //Map excluded facility ids and option ids to options list inside facilities list. Also enable all options.

        while (facilityPosition < facilitiesList.size) {
            var value = ""
            var keyValue = ""

            while (optionPosition < exclusionList[facilityPosition].size) {
                val rejectedFacilityId = exclusionList[facilityPosition][optionPosition].facility_id as String
                val rejectedOptionId = exclusionList[facilityPosition][optionPosition].options_id as String

                if (optionPosition == exclusionList[optionPosition].size - 1) {
                    value += "$rejectedFacilityId-$rejectedOptionId"
                } else {
                    keyValue = "$rejectedFacilityId-$rejectedOptionId"
                }
                hashMap[keyValue] = value
                optionPosition++
            }

            optionPosition = 0
            facilityPosition++
        }

        facilityPosition = 0
        optionPosition = 0

        while (facilityPosition < facilitiesList.size) {

            while (optionPosition < facilitiesList[facilityPosition].options?.size as Int) {

                val value = "${facilitiesList[facilityPosition].facility_id}-${facilitiesList[facilityPosition].options?.get(optionPosition)?.id}"
                facilitiesList[facilityPosition].options?.get(optionPosition)?.selectedFacility = hashMap.getOrDefault(value, "")
                facilitiesList[facilityPosition].options?.get(optionPosition)?.isEnabled = true
                optionPosition++

            }

            optionPosition = 0
            facilityPosition++
        }
        return facilitiesList
    }
}