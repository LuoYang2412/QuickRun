package com.luoyang.quickrun.data.model

/**
 * 运单
 */
data class FreightBill(
    val carsId: String,
    val createDate: String,
    val createUserId: String,
    val handover: String,
    val id: String,
    val manager: String,
    val outputDate: String,
    val outputNum: String,
    val route: String,
    val routeId: String,
    val state: Int,
    val warehouseId: Int
)