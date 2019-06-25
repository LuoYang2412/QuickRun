package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qinbang.quickrun.R
import com.qinbang.quickrun.utils.ToastUtil
import com.qinbang.quickrun.viewmodels.ResetPasswordViewModel
import kotlinx.android.synthetic.main.activity_reset_password.*
import timber.log.Timber

class ResetPasswordActivity : BaseActivity() {

    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, ResetPasswordActivity::class.java))
        }
    }

    private val viewModel by lazy { ViewModelProviders.of(this)[ResetPasswordViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val passwordInputTip = "建议密码不低于6位字母数字混合"
        textInputLayout3.editText?.afterTextChanged {
            if (!viewModel.passwordInputChange(it)) {
                textInputLayout3.error = passwordInputTip
            } else {
                textInputLayout3.error = ""
            }
        }
        textInputLayout4.editText?.afterTextChanged {
            if (!viewModel.passwordInputChange(it)) {
                textInputLayout4.error = passwordInputTip
            } else {
                textInputLayout4.error = ""
                if (viewModel.compartPassword(textInputLayout5.editText?.text.toString(), it)) {
                    textInputLayout5.error = ""
                } else {
                    textInputLayout5.error = "新密码不相同"
                }
            }
        }
        textInputLayout5.editText?.afterTextChanged {
            if (!viewModel.passwordInputChange(it)) {
                textInputLayout5.error = passwordInputTip
            } else {
                if (viewModel.compartPassword(textInputLayout4.editText?.text.toString(), it)) {
                    textInputLayout5.error = ""
                } else {
                    textInputLayout5.error = "新密码不相同"
                }
            }
        }

        viewModel.inputSuccess.observe(this, Observer {
            button5.isEnabled = it
        })

        viewModel.resultMsg.observe(this, Observer {
            button5.isEnabled = true
            when {
                it == "密码修改成功" -> finish()
                else -> {
                    ToastUtil.show(it)
                    Timber.d(it)
                }
            }
        })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.button5 -> {
                button5.isEnabled = false
                viewModel.resetPassword(
                    textInputLayout3.editText?.text.toString(),
                    textInputLayout5.editText?.text.toString()
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                afterTextChanged.invoke(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }
}
