package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qinbang.quickrun.R
import com.qinbang.quickrun.viewmodels.LoadingListViewModel
import kotlinx.android.synthetic.main.activity_risk_reporting.*

/**
 * 紧急报险
 */
class RiskReportingActivity : AppCompatActivity() {

    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, RiskReportingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk_reporting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MainActivity.mainViewModle.freightBillUnDone.observe(this, Observer {
            editText3.setText(it[0].num)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
