package com.luoyang.quickrun.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luoyang.quickrun.data.DeliveryManDataSource
import com.luoyang.quickrun.net.QuickRunNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    val loginResult = MutableLiveData<String>()
    var inputSuccess = MutableLiveData<Boolean>()

    fun login(mobileNo: String, password: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance().login(mobileNo, password)
                    if (resource.success) {
                        DeliveryManDataSource.save(resource.data!!)
                        loginResult.postValue("登录成功")
                    } else {
                        loginResult.postValue("登录失败：${resource.message}")
                    }
                }
            } catch (t: Throwable) {
                loginResult.value = "登录失败：${t.message}"
            }
        }
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
