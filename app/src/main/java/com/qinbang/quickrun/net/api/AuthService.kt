package com.qinbang.quickrun.net.api

import com.qinbang.quickrun.data.model.DeliveryMan
import com.qinbang.quickrun.net.Resource
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 授权服务
 */
interface AuthService {
    @FormUrlEncoded
    @POST("login")
    fun login(@Field("account") account: String, @Field("password") password: String, @Field("uuid") uuid: String = "0"): Call<Resource<DeliveryMan>>
}