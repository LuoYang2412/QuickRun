package com.qinbang.quickrun.net

import com.qinbang.quickrun.net.api.PlaceService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class QuickRunNetwork {

    private val placeService = ServiceCreator.create(PlaceService::class.java)

//    private val weatherService = ServiceCreator.create(WeatherService::class.java)

    suspend fun login(mobileNo: String, password: String) = placeService.getProvinces(mobileNo, password).await()

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }

    companion object {

        private var network: QuickRunNetwork? = null

        fun getInstance(): QuickRunNetwork {
            if (network == null) {
                synchronized(QuickRunNetwork::class.java) {
                    if (network == null) {
                        network = QuickRunNetwork()
                    }
                }
            }
            return network!!
        }

    }

}