package com.qinbang.quickrun.data

import android.preference.PreferenceManager
import androidx.core.content.edit
import com.google.gson.Gson
import com.qinbang.quickrun.QuickRunApplication
import com.qinbang.quickrun.data.model.DeliveryMan
import com.qinbang.quickrun.utils.EncryUtils

/**
 * 送货员数据
 */
class DeliveryManDataSource {
    private val mSharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(QuickRunApplication.application) }
    private val mGson by lazy { Gson() }
    private var deliveryMan: DeliveryMan? = null

    fun save(deliveryManObject: DeliveryMan) {
        val deliveryManEncryString =
            EncryUtils.getInstance()
                .encryptString(mGson.toJson(deliveryManObject), QuickRunApplication.application.packageName)
        mSharedPreferences.edit { putString("deliveryMan", deliveryManEncryString) }
        deliveryMan = null
    }

    private fun upData(deliveryManObject: DeliveryMan) {
        clean()
        save(deliveryManObject)
    }

    private fun clean() {
        mSharedPreferences.edit { clear() }
        deliveryMan = null
    }

    fun getData(): DeliveryMan? {
        if (deliveryMan == null) {
            val deliveryManEncryString =
                mSharedPreferences
                    .getString("deliveryMan", null)
            if (deliveryManEncryString != null) {
                val deliveryManDecryptString =
                    EncryUtils.getInstance()
                        .decryptString(deliveryManEncryString, QuickRunApplication.application.packageName)
                deliveryMan = mGson.fromJson(deliveryManDecryptString, DeliveryMan::class.java)
            }
        }
        return deliveryMan
    }
}

