package com.qinbang.quickrun.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

object AlertDialogUtil {
    fun show(context: Context, message: String, positiveButtonText: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(positiveButtonText, listener)
            .create()
            .show()
    }
}