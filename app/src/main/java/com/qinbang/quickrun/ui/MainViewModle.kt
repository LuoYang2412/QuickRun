package com.qinbang.quickrun.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qinbang.quickrun.data.model.Waybill

class MainViewModle : ViewModel() {
    val waybillLiveData by lazy { MutableLiveData<ArrayList<Waybill>>() }

    fun getWaybillData(auth: String) {
        val wayBills = ArrayList<Waybill>()
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        wayBills.add(Waybill("AAAAAA-BBBBB", "5646794646", "2019年6月1日10:10:10"))
        waybillLiveData.value = wayBills
    }
}