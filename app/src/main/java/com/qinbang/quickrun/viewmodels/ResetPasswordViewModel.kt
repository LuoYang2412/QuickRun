package com.qinbang.quickrun.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qinbang.quickrun.net.QuickRunNetwork
import com.qinbang.quickrun.ui.MainActivity2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetPasswordViewModel : ViewModel() {
    private val inputState = arrayOf(false, false)
    val inputSuccess = MutableLiveData<Boolean>()
    val resultMsg = MutableLiveData<String>()

    fun resetPassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_changePassword(newPassword, oldPassword, MainActivity2.mainViewModle.driver.value!!.uid)
                    if (resource.success) {
                        resultMsg.postValue("密码修改成功")
                        Handler(Looper.getMainLooper()).post { MainActivity2.mainViewModle.loginOut() }
                    } else {
                        resultMsg.postValue(resource.message)
                    }
                }
            } catch (t: Throwable) {
                resultMsg.value = t.message
            }
        }
    }

    fun passwordInputChange(password: String): Boolean {
        inputState[0] = isPassword(password)
        inputSuccess.value = inputState[0] && inputState[1]
        return isPassword(password)
    }

    fun compartPassword(password1: String, password2: String): Boolean {
        inputState[1] = password1 == password2
        inputSuccess.value = inputState[0] && inputState[1]
        return password1 == password2
    }

    private fun isPassword(password: String): Boolean {
        return password.length >= 6
    }
}