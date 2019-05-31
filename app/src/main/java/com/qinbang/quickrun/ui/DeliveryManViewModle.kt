package com.qinbang.quickrun.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qinbang.quickrun.data.DeliveryManDataSource
import com.qinbang.quickrun.data.model.DeliveryMan

/**
 * 送货员信息ViewModel
 */
class DeliveryManViewModle() : ViewModel() {
    val deliveryManLiveData = MutableLiveData<DeliveryMan>()
    private val deliveryManDataSource: DeliveryManDataSource by lazy { DeliveryManDataSource() }

    init {
        upData()
    }

    fun upData() {
        deliveryManLiveData.value = deliveryManDataSource.getData()
    }
}