package com.luoyang.quickrun.data.model

/**
 * 仓库
 */
data class Station(
    val id: String,
    val name: String,
    val sort: String,
    var status: StationStatus,
    var deliveryOrders: ArrayList<DeliveryOrder>
) {
    enum class StationStatus {
        ED, ING, WILL
    }
}
