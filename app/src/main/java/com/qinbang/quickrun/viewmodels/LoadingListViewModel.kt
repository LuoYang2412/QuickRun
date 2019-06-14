package com.qinbang.quickrun.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.data.model.Waybill
import com.qinbang.quickrun.net.QuickRunNetwork
import com.qinbang.quickrun.net.ResultOfView
import com.qinbang.quickrun.ui.MainActivity
import com.qinbang.quickrun.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingListViewModel : ViewModel() {
    val mGson by lazy { Gson() }
    val netResult = MutableLiveData<ResultOfView>()
    val orders = MutableLiveData<ArrayList<Order>>()
    val activeWayBill = MutableLiveData<Waybill>()

    /**
     * 获取当前货运单
     */
    fun getActWayBill() {
        viewModelScope.launch {
            try {
                netResult.value = withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_freightOrder_getAll("0", MainActivity.deliveryManViewModle.data.value!!.uid)
                    if (resource.success) {
                        val jsonElement = resource.data!!["freightOrderList"].asJsonArray[0]
                        activeWayBill.postValue(mGson.fromJson<Waybill>(jsonElement, Waybill::class.java))
                    }
                    ResultOfView(resource.success, "".plus(resource.message))
                }
            } catch (t: Throwable) {
                netResult.value = ResultOfView(false, "".plus(t.message))
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
                netResult.value = withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_order_getAll(freightOrderId, "", MainActivity.deliveryManViewModle.data.value!!.uid)
                    if (resource.success) {
                        orders.postValue(
                            mGson.fromJson(
                                resource.data?.get("orderList"),
                                object : TypeToken<ArrayList<Order>>() {}.type
                            )
                        )
                    }
                    ResultOfView(resource.success, "".plus(resource.message))
                }
            } catch (t: Throwable) {
                netResult.value = ResultOfView(false, "".plus(t.message))
            }
        }
    }
    /**
     * 修改订单状态
     * @param pickUpId ""表示装货完成状态，有值表示提货点下货完成
     */
    fun setWaybillOrOrderStatus(freightOrderId: String, pickUpId: String = "") {
        viewModelScope.launch {
            try {
                netResult.value = withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance().app_order_inDistribution(
                        freightOrderId,
                        pickUpId, MainActivity.deliveryManViewModle.data.value!!.uid
                    )
                        ResultOfView(resource.success, "".plus(resource.message),Constants.SET_WAYBILL_OR_ORDER_STATUS)
                }
            } catch (t: Throwable) {
                netResult.value = ResultOfView(false, "".plus(t.message),Constants.SET_WAYBILL_OR_ORDER_STATUS)
            }
        }
    }
}