package com.luoyang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luoyang.quickrun.R
import com.luoyang.quickrun.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

/**
 * 登录
 */
class LoginActivity : BaseActivity() {

    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private val viewModel by lazy { ViewModelProviders.of(this).get(LoginViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mobileNo.afterTextChanged {
            viewModel.loginDataChanged(
                mobileNo.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            viewModel.loginDataChanged(
                mobileNo.text.toString(),
                password.text.toString()
            )
        }

        login.setOnClickListener {
            viewModel.login(mobileNo.text.toString(), password.text.toString())
            loading.visibility = View.VISIBLE
        }

        //所有输入正确
        viewModel.inputSuccess.observe(this, Observer {
            login.isEnabled = it
        })

        //登录接口返回
        viewModel.loginResult.observe(this, Observer {
            when {
                it == "登录成功" -> {
                    loading.visibility = View.GONE
                    MainActivity2.mainViewModle.updataDriverInfo()
                    finish()
                }
                else -> {
                    loading.visibility = View.GONE
                    showToast(it)
                }
            }
        })
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
