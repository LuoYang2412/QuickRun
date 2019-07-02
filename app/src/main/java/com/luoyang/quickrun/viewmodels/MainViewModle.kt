package com.luoyang.quickrun.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.luoyang.quickrun.data.DeliveryManDataSource
import com.luoyang.quickrun.data.model.DeliveryMan
import com.luoyang.quickrun.data.model.FreightBill
import com.luoyang.quickrun.net.QuickRunNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModle : ViewModel() {
    val errorMsg = MutableLiveData<String>()
    //司机
    val driver = MutableLiveData<DeliveryMan>()
    //已完成运单
    val freightBillDone = MutableLiveData<ArrayList<FreightBill>>()
    //未完成运单
    val freightBillUnDone = MutableLiveData<ArrayList<FreightBill>>()

    init {
        driver.value = getDriverInfo()
    }

    /**
     * 更新司机
     */
    fun updataDriverInfo() {
        driver.value = getDriverInfo()
        if (driver.value == null) {//司机null，清空货运单
            if (freightBillDone.value != null) {
                freightBillDone.value = freightBillDone.value!!.apply { clear() }
            }
            if (freightBillUnDone.value != null) {
                freightBillUnDone.value = freightBillUnDone.value!!.apply { clear() }
            }
        }
    }

    /**
     * 清除司机
     */
    private fun clearDriverInfo() {
        DeliveryManDataSource.clear()
        updataDriverInfo()
    }

    fun loginOut() {
        clearDriverInfo()
        logined()
    }

    /**
     * 获取司机
     */
    private fun getDriverInfo(): DeliveryMan? = DeliveryManDataSource.getData()

    /**
     * 获取历史运单号
     */
    fun getFreightBillDone() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_freightOrder_getAll("1", driver.value!!.uid)
                    if (resource.success) {
                        val freightOrderList = resource.data!!["freightOrderList"]
                        val type = object : TypeToken<ArrayList<FreightBill>>() {}.type
                        val fromJson = Gson().fromJson<ArrayList<FreightBill>>(
                            freightOrderList,
                            type
                        )
                        freightBillDone.postValue(fromJson)
                    } else {
                        errorMsg.postValue(resource.message)
                    }
                }
            } catch (t: Throwable) {
                errorMsg.value = t.message
            }
        }
    }

    /**
     * 获取未完成运单号
     */
    fun getFreightBillUnDone() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_freightOrder_getAll("0", driver.value!!.uid)
                    if (resource.success) {
                        val jsonElement = resource.data!!["freightOrderList"]
                        freightBillUnDone.postValue(
                            Gson().fromJson(
                                jsonElement,
                                object : TypeToken<ArrayList<FreightBill>>() {}.type
                            )
                        )
                    }
                }
            } catch (t: Throwable) {
                errorMsg.value = t.message
            }
        }
    }

    fun getData() {
        getFreightBillDone()
        getFreightBillUnDone()
    }

    //登录状态
    fun logined(): Boolean {
        if (driver.value == null) {
            errorMsg.postValue("未登录")
        }
        return driver.value != null
    }

    //有未完成货单
    fun haveFreightBillUnDone() = freightBillUnDone.value != null && freightBillUnDone.value!!.size > 0
}