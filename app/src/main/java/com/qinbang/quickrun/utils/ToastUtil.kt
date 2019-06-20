package com.qinbang.quickrun.utils

import android.content.Context
import android.widget.Toast

object ToastUtil {
    fun show(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}