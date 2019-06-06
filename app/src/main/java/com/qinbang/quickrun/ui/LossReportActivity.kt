package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.qinbang.quickrun.R
import kotlinx.android.synthetic.main.activity_loss_report.*

/**
 * 损耗申报
 */
class LossReportActivity : AppCompatActivity() {

    companion object {
        fun goIn(context: Context, orderId: String? = null) {
            val intent = Intent(context, LossReportActivity::class.java)
            intent.putExtra("orderId", orderId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loss_report)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val orderId = intent.getStringExtra("orderId")
        if (orderId != null) {
            editText.setText(orderId)
            editText.isEnabled = false
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
}
