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
    private val inputSuccess = arrayOf(true, false)
    val resultMsg = MutableLiveData<String>()
    val inputStatus = MutableLiveData<Boolean>()

    fun resetPhone(newPhone: String) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    val resource = QuickRunNetwork.getInstance()
                        .app_changeMobilePhone(newPhone, MainActivity2.mainViewModle.driver.value!!.uid)
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

    fun phoneInputChange(mobileNo: String, index: Int): Boolean {
        inputSuccess[index] = isMobileNo(mobileNo)
        inputStatus.value = inputSuccess[0] && inputSuccess[1]
        return isMobileNo(mobileNo)
    }

    private fun isMobileNo(mobileNo: String): Boolean {
        return mobileNo.length == 11
    }
}