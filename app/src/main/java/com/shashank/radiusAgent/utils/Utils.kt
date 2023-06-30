package com.shashank.radiusAgent.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun CoroutineScope.launchOnMain(call:()->Unit){
    launch (Dispatchers.Main){
        call.invoke()
    }
}
