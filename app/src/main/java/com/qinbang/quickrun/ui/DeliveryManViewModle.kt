package com.qinbang.quickrun.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qinbang.quickrun.data.DeliveryManDataSource
import com.qinbang.quickrun.data.model.DeliveryMan

/**
 * 送货员信息ViewModel
 */
class DeliveryManViewModle() : ViewModel() {
    val data by lazy { MutableLiveData<DeliveryMan>() }
    private val deliveryManDataSource: DeliveryManDataSource by lazy { DeliveryManDataSource() }

    init {
        upData()
    }

    /**
     * 更新送货员信息
     */
    fun upData() {
        data.value = deliveryManDataSource.getData()
    }
}