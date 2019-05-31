package com.qinbang.quickrun.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qinbang.quickrun.data.DeliveryManDataSource
import com.qinbang.quickrun.net.QuickRunNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var loginResult = MutableLiveData<Boolean>()
    var inputSuccess = MutableLiveData<Boolean>()
    val deliveryManDataSource: DeliveryManDataSource by lazy { DeliveryManDataSource() }

    fun login(mobileNo: String, password: String) {
        viewModelScope.launch {
            loginResult.value = withContext(Dispatchers.IO) {
                val resource = QuickRunNetwork.getInstance().login(mobileNo, password)
                if (resource.success) {
                    deliveryManDataSource.save(resource.data!!)
                }
                resource.success
            }
        }
    }

    fun loginOut() {

    }

    fun loginDataChanged(mobileNo: String, password: String) {
        if (!isMobileNo(mobileNo)) {
            inputSuccess.value = false
        } else {
            inputSuccess.value = isPassword(password)
        }
    }

    private fun isMobileNo(mobileNo: String): Boolean {
        return mobileNo.length == 11
    }

    private fun isPassword(password: String): Boolean {
        return password.length >= 6
    }

}
