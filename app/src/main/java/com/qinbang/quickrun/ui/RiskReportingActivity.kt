package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    val loadingListViewModel by lazy { ViewModelProviders.of(this).get(LoadingListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk_reporting)

        loadingListViewModel.getActWayBill()
        loadingListViewModel.activeWayBill.observe(this, Observer {
            editText3.setText(it.num)
        })
    }
}
