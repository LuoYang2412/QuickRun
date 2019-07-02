package com.luoyang.quickrun.data

import android.preference.PreferenceManager
import androidx.core.content.edit
import com.google.gson.Gson
import com.luoyang.quickrun.QuickRunApplication
import com.luoyang.quickrun.data.model.DeliveryMan

/**
 * 送货员数据
 */
object DeliveryManDataSource {
    private val mSharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(QuickRunApplication.application) }
    private val mGson by lazy { Gson() }
    private var deliveryMan: DeliveryMan? = null

    fun save(deliveryManObject: DeliveryMan) {
        mSharedPreferences.edit { putString("deliveryMan", mGson.toJson(deliveryManObject)) }
        deliveryMan = null
    }

    private fun upData(deliveryManObject: DeliveryMan) {
        clear()
        save(deliveryManObject)
    }

    fun clear() {
        mSharedPreferences.edit { clear() }
        deliveryMan = null
    }

    fun getData(): DeliveryMan? {
        if (deliveryMan == null) {
            val deliveryManString =
                mSharedPreferences
                    .getString("deliveryMan", null)
            if (deliveryManString != null) {
                deliveryMan = mGson.fromJson(deliveryManString, DeliveryMan::class.java)
            }
        }
        return deliveryMan
    }
}

