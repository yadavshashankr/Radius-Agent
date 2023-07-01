package com.shashank.radiusAgent.network.api

import com.shashank.radiusAgent.network.model.MainModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("iranjith4/ad-assignment/db")
    suspend fun getCategory() :Response<MainModel>

}