package com.qinbang.quickrun.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.data.model.Station

class TransportRouteViewModel : ViewModel() {
    val stationsLiveData by lazy { MutableLiveData<ArrayList<Station>>() }

    fun getData() {
        val orders1 = ArrayList<Order>()
        orders1.add(Order("465123", true))
        orders1.add(Order("adsgewtq"))
        orders1.add(Order("fat45326", true))
        orders1.add(Order("465123", true))
        orders1.add(Order("adsgewtq"))
        orders1.add(Order("fat45326", true))
        orders1.add(Order("465123", true))
        orders1.add(Order("adsgewtq"))
        orders1.add(Order("fat45326", true))
        orders1.add(Order("465123", true))
        orders1.add(Order("adsgewtq"))
        orders1.add(Order("fat45326", true))
        val orders2 = ArrayList<Order>()
        orders2.add(Order("fat45326", true))
        orders2.add(Order("465123", true))
        orders2.add(Order("adsgewtq"))
        orders2.add(Order("fat45326", true))
        orders2.add(Order("adsgewtq"))
        orders2.add(Order("fat45326", true))
        orders2.add(Order("adsgewtq"))
        orders2.add(Order("fat45326", true))
        val station1 = Station("IDS11245435", "王宽站", Station.StationStatus.ED, orders1)
        val station2 = Station("879646", "花溪站", Station.StationStatus.ING, orders2)
        val station3 = Station("54542qy43", "三江口站", Station.StationStatus.WILL, orders1)
        val station4 = Station("4674897", "秦山站", Station.StationStatus.WILL, orders2)
        val stations = ArrayList<Station>()
        stations.add(station1)
        stations.add(station2)
        stations.add(station3)
        stations.add(station4)
        stations.add(station3)
        stations.add(station3)
        stations.add(station4)
        stations.add(station3)
        stations.add(station4)
        stations.add(station1)
        stationsLiveData.value = stations
    }
}