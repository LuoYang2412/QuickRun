package com.luoyang.quickrun.ui

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luoyang.quickrun.QuickRunApplication
import com.luoyang.quickrun.utils.AlertDialogUtil
import com.luoyang.quickrun.utils.Android_ID_Utils
import com.luoyang.quickrun.utils.ToastUtil
import timber.log.Timber

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuickRunApplication.application.addActivity(this)
        initUtil()
        checkVM()
    }

    private fun initUtil() {
        ToastUtil.init(this)
        AlertDialogUtil.init(this)
    }

    override fun onDestroy() {
        QuickRunApplication.application.removeTopActivity()
        super.onDestroy()
    }

    private fun checkVM() {
        if (Android_ID_Utils.notHasBlueTooth() || Android_ID_Utils.notHasLightSensorManager(this) ||
            Android_ID_Utils.isFeatures() || Android_ID_Utils.checkIsNotRealPhone()
        ) {
            AlertDialogUtil.show(
                "检查到您的设备违规,将限制您的所有功能使用!",
                "退出",
                false,
                DialogInterface.OnClickListener { dialog, which ->
                    QuickRunApplication.application.exit()
                })
            return
        }
    }

    protected fun showToast(msg: String) {
        Timber.d(msg)
        ToastUtil.show(msg)
    }
}
