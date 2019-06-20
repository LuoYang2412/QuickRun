package com.qinbang.quickrun.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qinbang.quickrun.data.model.FreightBill
import com.qinbang.quickrun.data.model.DeliveryOrder
import com.qinbang.quickrun.net.QuickRunNetwork
import com.qinbang.quickrun.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoadingListViewModel : ViewModel() {
    val mGson by lazy { Gson() }
    val resultMsg = MutableLiveData<String>()
    val orders = MutableLiveData<ArrayList<DeliveryOrder>>()

    /**
     * 获取订单
     * @param pickUpId 提货单ID ""表示所有提货点
     */
    fun getOrders(freightOrderId: String, pickUpId: String = "") {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_order_getAll(freightOrderId, "", MainActivity.mainViewModle.driver.value!!.uid)
                    if (resource.success) {
                        orders.postValue(
                            mGson.fromJson(
                                resource.data?.get("orderList"),
                                object : TypeToken<ArrayList<DeliveryOrder>>() {}.type
                            )
                        )
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
    fun setWaybillOrOrderStatus(freightOrderId: String, pickUpId: String = "") {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance().app_order_inDistribution(
                        freightOrderId,
                        pickUpId, MainActivity.mainViewModle.driver.value!!.uid
                    )
                    if (resource.success) {
                        MainActivity.mainViewModle.getFreightBillUnDone()
                    } else {
                        resultMsg.postValue(resource.message)
                    }
                }
            } catch (t: Throwable) {
                resultMsg.value = t.message
            }
        }
    }
}