package com.qinbang.quickrun.data.model

/**
 * 送货员信息
 */
data class DeliveryMan(
    val account: String,
    val address: String,
    val areaCity: String,
    val areaCounty: String,
    val areaProvince: String,
    val createDate: String,
    val email: String,
    val image: List<String>,
    val mobilePhone: String,
    val realName: String,
    val remarks: String,
    val sex: String,
    val telePhone: String,
    val token: String,
    val uid: String,
    val userName: String,
    val userTypeId: String
)