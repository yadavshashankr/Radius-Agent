package com.shashank.radiusAgent.contracts

interface SelectedOptionsContract {
    fun disableSelectedOptionsState(valueFacilityID : String, valueOptionsID : String, selectedPosition : Int)
}