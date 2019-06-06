package com.qinbang.quickrun.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qinbang.quickrun.data.model.Order

class LoadingListViewModel : ViewModel() {
    val orders = MutableLiveData<ArrayList<Order>>()
    fun getData() {
        //TODO 获取数据
        val orderList = ArrayList<Order>()
        orderList.add(Order("5313131313"))
        orderList.add(Order("878951531", true))
        orderList.add(Order("354236624drg", false))
        orderList.add(Order("5313131313"))
        orderList.add(Order("878951531", true))
        orderList.add(Order("354236624drg", false))
        orderList.add(Order("5313131313"))
        orderList.add(Order("878951531", true))
        orderList.add(Order("354236624drg", false))
        orderList.add(Order("5313131313"))
        orderList.add(Order("878951531", true))
        orderList.add(Order("354236624drg", false))
        orders.value = orderList
    }
}