package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.qinbang.quickrun.R
import com.qinbang.quickrun.viewmodels.MobilePhoneChangeViewModel

/**
 * 手机号码修改
 */
class MobilePhoneChangeActivity : AppCompatActivity() {
    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, MobilePhoneChangeActivity::class.java))
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(MobilePhoneChangeViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_phone_change)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
