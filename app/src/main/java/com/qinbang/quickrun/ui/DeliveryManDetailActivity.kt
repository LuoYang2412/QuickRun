package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qinbang.quickrun.R
import kotlinx.android.synthetic.main.activity_delivery_man_detail.*

class DeliveryManDetailActivity : AppCompatActivity() {

    companion object {
        fun goIn(context: Context, id: String) {
            val intent = Intent(context, DeliveryManDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_man_detail)
        textView.text = intent.getStringExtra("id")
    }
}
