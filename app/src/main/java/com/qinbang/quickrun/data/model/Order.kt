package com.qinbang.quickrun.data.model

/**
 * 订单
 */
data class Order(
    val adress: String,
    val freightOrderId: String,
    val id: String,
    val pickUpId: String,
    val shipState: Int,
    val shipmentNumber: String,
    val warehouseAddress: String,
    val warehouseCity: String,
    val warehouseCountry: String,
    val warehouseName: String,
    val warehouseProvince: String,
    val warehouseTown: String
)