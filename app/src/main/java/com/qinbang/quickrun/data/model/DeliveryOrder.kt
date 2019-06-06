package com.qinbang.quickrun.data.model

/**
 * 运货单
 */
data class DeliveryOrder(
    val id: String,
    val name: String,
    val time: String,
    val stations: ArrayList<Station>
)