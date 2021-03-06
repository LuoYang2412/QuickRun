package com.luoyang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luoyang.quickrun.R
import com.luoyang.quickrun.viewmodels.ResetPhoneViewModel
import kotlinx.android.synthetic.main.activity_reset_phone.*

/**
 * 手机号码修改
 */
class ResetPhoneActivity : BaseActivity() {
    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, ResetPhoneActivity::class.java))
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(ResetPhoneViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_phone)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textInputLayout.editText?.setText(MainActivity2.mainViewModle.driver.value!!.mobilePhone)
        val errorTip = "请输入11位手机号码"
        textInputLayout.editText?.afterTextChanged {
            if (viewModel.phoneInputChange(it, 0)) {
                textInputLayout.error = ""
            } else {
                textInputLayout.error = errorTip
            }
        }
        textInputLayout2.editText?.afterTextChanged {
            if (viewModel.phoneInputChange(it, 1)) {
                textInputLayout2.error = ""
                countdownButton.isGone = false
            } else {
                textInputLayout2.error = errorTip
                countdownButton.isGone = true
            }
        }
        textInputLayout6.editText?.afterTextChanged {
            viewModel.vcodeInputChange(it)
        }
        button3.setOnClickListener(this::onClick)
        countdownButton.setOnClickListener(this::onClick)

        viewModel.inputStatus.observe(this, Observer {
            button3.isEnabled = it
        })
        viewModel.resultMsg.observe(this, Observer {
            when {
                it == "修改手机号成功" -> finish()
                it == "发送验证码失败" -> {
                    countdownButton.resetCounter()
                    showToast(it)
                }
                else -> {
                    showToast(it)
                }
            }
        })
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.button3 -> {
                view.isEnabled = false
                viewModel.resetPhone(
                    textInputLayout2.editText?.text.toString(),
                    textInputLayout6.editText?.text.toString()
                )
                Handler().postDelayed({ view.isEnabled = true }, 500)
            }
            R.id.countdownButton -> {
                countdownButton.sendVerifyCode()
                viewModel.send_message(textInputLayout2.editText?.text.toString(), 5)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
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
