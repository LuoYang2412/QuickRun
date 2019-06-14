package com.qinbang.quickrun.data.model

/**
 * 运单信息
 */
data class Waybill(
    val createDate: String,
    val id: String,
    val num: String,
    val route: String,
    val state: Int,
    val warehouseId: Int
)