package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.qinbang.quickrun.R
import kotlinx.android.synthetic.main.activity_risk_reporting.*

/**
 * 紧急报险
 */
class RiskReportingActivity : BaseActivity() {

    companion object {
        fun goIn(context: Context, freightOrderId: String, freightOrderNum: String) {
            val intent = Intent(context, RiskReportingActivity::class.java)
            intent.putExtra("freightOrderId", freightOrderId)
            intent.putExtra("freightOrderNum", freightOrderNum)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk_reporting)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editText3.setText(intent.getStringExtra("freightOrderNum"))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
