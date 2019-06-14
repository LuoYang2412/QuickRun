package com.qinbang.quickrun.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.qinbang.quickrun.data.model.Waybill
import com.qinbang.quickrun.net.QuickRunNetwork
import com.qinbang.quickrun.net.ResultOfView
import com.qinbang.quickrun.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModle : ViewModel() {
    val netResult = MutableLiveData<ResultOfView>()
    val waybillLiveData by lazy { MutableLiveData<ArrayList<Waybill>>() }

    /**
     * 获取历史运单号
     */
    fun getWaybillData() {
        viewModelScope.launch {
            try {
                netResult.value = withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_freightOrder_getAll("1", MainActivity.deliveryManViewModle.data.value!!.uid)
                    if (resource.success) {
                        val freightOrderList = resource.data!!["freightOrderList"]
                        val type = object : TypeToken<ArrayList<Waybill>>() {}.type
                        val fromJson = Gson().fromJson<ArrayList<Waybill>>(
                            freightOrderList,
                            type
                        )
                        waybillLiveData.postValue(fromJson)
                    }
                    ResultOfView(resource.success, resource.message + "")
                }
            } catch (t: Throwable) {
                netResult.value = ResultOfView(false, t.message + "")
            }
        }
    }
}