package com.qinbang.quickrun.data.model

/**
 * 仓库
 */
data class Station(
    val id: String,
    val name: String,
    val sort: String,
    var status: StationStatus = StationStatus.WILL,
    var orders: ArrayList<Order>
) {
    enum class StationStatus {
        ED, ING, WILL
    }
}
