package com.qinbang.quickrun.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qinbang.quickrun.R
import com.qinbang.quickrun.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        mobileNo.afterTextChanged {
            loginViewModel.loginDataChanged(
                mobileNo.text.toString(),
                password.text.toString()
            )
        }

        password.afterTextChanged {
            loginViewModel.loginDataChanged(
                mobileNo.text.toString(),
                password.text.toString()
            )
        }

        login.setOnClickListener {
            Thread.sleep(500)
            loginViewModel.login(mobileNo.text.toString(), password.text.toString())
            loading.visibility = View.VISIBLE
        }

        //所有输入正确
        loginViewModel.inputSuccess.observe(this, Observer {
            login.isEnabled = it
        })

        //登录接口返回
        loginViewModel.loginResult.observe(this, Observer {
            if (it) {
                MainActivity.deliveryManViewModle.upData()
                finish()
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
