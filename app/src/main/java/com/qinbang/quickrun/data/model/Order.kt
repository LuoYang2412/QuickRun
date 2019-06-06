package com.qinbang.quickrun.data.model

/**
 * 订单
 */
data class Order(
    val id: String,
    var haveOutbound: Boolean = false
)