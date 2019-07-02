package com.luoyang.quickrun.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.luoyang.quickrun.data.model.DeliveryOrder
import com.luoyang.quickrun.data.model.Station
import com.luoyang.quickrun.net.QuickRunNetwork
import com.luoyang.quickrun.ui.MainActivity2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TransportRouteViewModel : ViewModel() {
    private val mGson by lazy { Gson() }
    private var orders = ArrayList<DeliveryOrder>()
    private var stations = ArrayList<Station>()
    val getDataSuccess = MutableLiveData<Array<Boolean>>()
    val stationsLiveData = MutableLiveData<ArrayList<Station>>()
    val resultMsg = MutableLiveData<String>()
    private val success = arrayOf(false, false)
    var ingStationPosition = 0
    var routesDone = false
    /**
     * 获取站点
     */
    fun getStation(freightOrderId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_route_getRoute(freightOrderId, MainActivity2.mainViewModle.driver.value!!.uid)
                    if (resource.success) {
                        val ingStation = resource.data!!["sort"].asInt
                        val routeStr = resource.data["route"].asString
                        val routeJsO = mGson.fromJson(routeStr, JsonObject::class.java)
                        val stations1 = mGson.fromJson<ArrayList<Station>>(
                            routeJsO["nodes"],
                            object : TypeToken<ArrayList<Station>>() {}.type
                        )
                        when {
                            ingStation < 0 -> ingStationPosition = 0
                            ingStation > 0 && ingStation <= stations1.size -> ingStationPosition = ingStation - 1
                            ingStation > stations1.size -> {
                                routesDone = ingStation == 9999
                                ingStationPosition = stations1.size - 1
                            }
                        }
                        stations1.map {
                            val sort = it.sort.toInt()
                            when {
                                sort < ingStation -> it.status = Station.StationStatus.ED
                                sort == ingStation -> it.status = Station.StationStatus.ING
                                sort > ingStation -> it.status = Station.StationStatus.WILL
                            }
                            it.deliveryOrders = ArrayList()
                        }
                        stations = stations1
                        success[0] = true
                        getDataSuccess.postValue(success)
                    } else {
                        resultMsg.postValue(resource.message)
                    }
                }
            } catch (t: Throwable) {
                resultMsg.value = t.message
            }
        }
    }


    /**
     * 获取订单
     * @param pickUpId 提货单ID ""表示所有提货点
     */
    fun getOrders(freightOrderId: String, pickUpId: String = "") {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_order_getAll(freightOrderId, "", MainActivity2.mainViewModle.driver.value!!.uid)
                    if (resource.success) {
                        val orders1 = mGson.fromJson<ArrayList<DeliveryOrder>>(
                            resource.data?.get("orderList"),
                            object : TypeToken<ArrayList<DeliveryOrder>>() {}.type
                        )
                        orders = orders1
                        success[1] = true
                        getDataSuccess.postValue(success)
                    } else {
                        resultMsg.postValue(resource.message)
                    }
                }
            } catch (t: Throwable) {
                resultMsg.value = t.message
            }
        }
    }


    /**
     * 修改运单状态
     * @param pickUpId ""表示装货完成状态，有值表示提货点下货完成
     */
    fun setWaybillOrOrderStatus(freightOrderId: String, pickUpId: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance().app_order_inDistribution(
                        freightOrderId,
                        pickUpId, MainActivity2.mainViewModle.driver.value!!.uid
                    )
                    if (resource.success) {
                        Handler(Looper.getMainLooper()).post { MainActivity2.mainViewModle.getData() }
                    } else {
                        resultMsg.postValue(resource.message)
                    }
                }
            } catch (t: Throwable) {
                resultMsg.value = t.message
            }
        }
    }

    fun getData(freightOrderId: String) {
        success[0] = false
        success[1] = false
        getStation(freightOrderId)
        getOrders(freightOrderId)
    }

    fun packageData() {
        stations.map { station ->
            orders.map { order ->
                if (order.pickUpId == station.id) {
                    station.deliveryOrders.add(order)
                }
            }
        }
        stationsLiveData.value = stations
    }
}