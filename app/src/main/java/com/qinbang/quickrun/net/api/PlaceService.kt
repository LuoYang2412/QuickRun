package com.qinbang.quickrun.net.api

import com.qinbang.quickrun.data.model.DeliveryMan
import com.qinbang.quickrun.net.Resource
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("me/")
    fun getProvinces(@Query("mobileNo") mobileNo: String, @Query("password") password: String): Call<Resource<DeliveryMan>>

}