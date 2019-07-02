package com.luoyang.quickrun.viewmodels

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luoyang.quickrun.net.QuickRunNetwork
import com.luoyang.quickrun.ui.MainActivity2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResetPhoneViewModel : ViewModel() {
    private val inputSuccess = arrayOf(true, false, false)
    val resultMsg = MutableLiveData<String>()
    val inputStatus = MutableLiveData<Boolean>()

    fun resetPhone(newPhone: String, code: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_changeMobilePhone(
                            MainActivity2.mainViewModle.driver.value!!.mobilePhone,
                            newPhone,
                            MainActivity2.mainViewModle.driver.value!!.uid,
                            code
                        )
                    if (resource.success) {
                        resultMsg.postValue("修改手机号成功")
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

    fun send_message(mobileNo: String, type: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance().send_message(mobileNo, type)
                    if (resource.success) {
                        resultMsg.postValue("发送验证码成功")
                    } else {
                        resultMsg.value = "发送验证码失败"
                    }
                }
            } catch (t: Throwable) {
                resultMsg.value = "发送验证码失败"
            }
        }
    }

    fun phoneInputChange(mobileNo: String, index: Int): Boolean {
        inputSuccess[index] = isMobileNo(mobileNo)
        inputStatus.value = inputSuccess[0] && inputSuccess[1] && inputSuccess[2]
        return isMobileNo(mobileNo)
    }

    fun vcodeInputChange(vCode: String) {
        inputSuccess[2] = vCode.length > 3
        inputStatus.value = inputSuccess[0] && inputSuccess[1] && inputSuccess[2]
    }

    private fun isMobileNo(mobileNo: String): Boolean {
        return mobileNo.length == 11
    }
}