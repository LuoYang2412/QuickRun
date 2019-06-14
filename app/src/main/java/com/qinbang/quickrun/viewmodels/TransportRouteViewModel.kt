package com.qinbang.quickrun.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.qinbang.quickrun.data.model.Station
import com.qinbang.quickrun.net.QuickRunNetwork
import com.qinbang.quickrun.net.ResultOfView
import com.qinbang.quickrun.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.Int as Int1

class TransportRouteViewModel : ViewModel() {
    val mGson by lazy { Gson() }
    val netResult = MutableLiveData<ResultOfView>()
    val stationsLiveData by lazy { MutableLiveData<ArrayList<Station>>() }
    val currentStation = MutableLiveData<kotlin.Int>()
    /**
     * 获取运输路线
     */
    fun getRoute(freightOrderId: String) {
        viewModelScope.launch {
            try {
                netResult.value = withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_route_getRoute(freightOrderId, MainActivity.deliveryManViewModle.data.value!!.uid)
                    if (resource.success) {
                        val routeStr = resource.data!!["route"].asString
                        val routeJsO = mGson.fromJson(routeStr, JsonObject::class.java)
                        stationsLiveData.postValue(
                            mGson.fromJson(
                                routeJsO["nodes"],
                                object : TypeToken<ArrayList<Station>>() {}.type
                            )
                        )
                        currentStation.postValue(resource.data["sort"].asInt)
                    }
                    ResultOfView(resource.success, "${resource.message}")
                }
            } catch (t: Throwable) {
                netResult.value = ResultOfView(false, "${t.message}")
            }
        }
    }

    fun getData() {
//        val orders1 = ArrayList<Order>()
//        orders1.add(Order("465123", true))
//        orders1.add(Order("adsgewtq"))
//        orders1.add(Order("fat45326", true))
//        orders1.add(Order("465123", true))
//        orders1.add(Order("adsgewtq"))
//        orders1.add(Order("fat45326", true))
//        orders1.add(Order("465123", true))
//        orders1.add(Order("adsgewtq"))
//        orders1.add(Order("fat45326", true))
//        orders1.add(Order("465123", true))
//        orders1.add(Order("adsgewtq"))
//        orders1.add(Order("fat45326", true))
//        val orders2 = ArrayList<Order>()
//        orders2.add(Order("fat45326", true))
//        orders2.add(Order("465123", true))
//        orders2.add(Order("adsgewtq"))
//        orders2.add(Order("fat45326", true))
//        orders2.add(Order("adsgewtq"))
//        orders2.add(Order("fat45326", true))
//        orders2.add(Order("adsgewtq"))
//        orders2.add(Order("fat45326", true))
//        val station1 = Station("IDS11245435", "王宽站", Station.StationStatus.ED, orders1)
//        val station2 = Station("879646", "花溪站", Station.StationStatus.ING, orders2)
//        val station3 = Station("54542qy43", "三江口站", Station.StationStatus.WILL, orders1)
//        val station4 = Station("4674897", "秦山站", Station.StationStatus.WILL, orders2)
//        val stations = ArrayList<Station>()
//        stations.add(station1)
//        stations.add(station2)
//        stations.add(station3)
//        stations.add(station4)
//        stations.add(station3)
//        stations.add(station3)
//        stations.add(station4)
//        stations.add(station3)
//        stations.add(station4)
//        stations.add(station1)
//        stationsLiveData.value = stations
    }
}