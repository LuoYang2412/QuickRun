package com.qinbang.quickrun.net.api

import com.google.gson.JsonObject
import com.qinbang.quickrun.net.Resource
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DeliveryService {
    @FormUrlEncoded
    @POST("app_freightOrder_getAll")
    fun app_freightOrder_getAll(@Field("state") state: String, @Field("userId") userId: String): Call<Resource<JsonObject>>

    @FormUrlEncoded
    @POST("app_order_getAll")
    fun app_order_getAll(
        @Field("freightOrderId") freightOrderId: String, @Field("pickUpId") pickUpId: String,
        @Field("userId") userId: String
    ): Call<Resource<JsonObject>>

    @FormUrlEncoded
    @POST("app_route_getRoute")
    fun app_route_getRoute(
        @Field("freightOrderId") freightOrderId: String,
        @Field("userId") userId: String
    ): Call<Resource<JsonObject>>

    @FormUrlEncoded
    @POST("app_order_inDistribution")
    fun app_order_inDistribution(
        @Field("freightOrderId") freightOrderId: String,
        @Field("pickId") pickId: String,
        @Field("userId") userId: String
    ):Call<Resource<JsonObject>>
}