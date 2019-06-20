package com.qinbang.quickrun.data.model

/**
 * 运单
 */
data class FreightBill(
    val createDate: String,
    val id: String,
    val num: String,
    val route: String,
    val state: Int,
    val warehouseId: Int
)