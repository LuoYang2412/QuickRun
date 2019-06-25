package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qinbang.quickrun.R
import com.qinbang.quickrun.ui.adapters.DeliveryOrderListAdapter
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.utils.ToastUtil
import com.qinbang.quickrun.viewmodels.LoadingListViewModel
import kotlinx.android.synthetic.main.activity_loading_list.*

/**
 * 装货清单
 */
class LoadingListActivity : BaseActivity() {

    companion object {
        fun goIn(context: Context, position: Int) {
            val intent = Intent(context, LoadingListActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(LoadingListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myLoadingListAdapter = DeliveryOrderListAdapter()
        myLoadingListAdapter.isFirstOnly(false)
        myLoadingListAdapter.openLoadAnimation()
        recyclerView2.addItemDecoration(LinearSpacesItemDecoration())
        recyclerView2.adapter = myLoadingListAdapter

        val freightsPosition = intent.getIntExtra("position", 0)

        MainActivity2.mainViewModle.freightBillUnDone.observe(this, Observer { freights ->
            textView11.text = resources.getString(R.string.freight_id).plus(freights[freightsPosition].num)
            if (myLoadingListAdapter.footerLayoutCount == 0) {
                val footerView = layoutInflater.inflate(R.layout.layout_radius_btn, recyclerView2, false)
                myLoadingListAdapter.addFooterView(footerView)
            }
            myLoadingListAdapter.footerLayout.findViewById<TextView>(R.id.radius_btn_content_textView14).text =
                when (freights[freightsPosition].state) {
                    -1 -> "确认出库"
                    0 -> "确认配送"
                    1 -> "查看路线"
                    else -> ""
                }
            myLoadingListAdapter.footerLayout.findViewById<CardView>(R.id.radius_btn_cardView)
                .setOnClickListener(View.OnClickListener {
                    it.isEnabled = false
                    if (freights[freightsPosition].state == 1) {
                        TransportRouteActivity.goIn(this, freightsPosition)
                        finish()
                    } else {
                        viewModel.setWaybillOrOrderStatus(freights[freightsPosition].id)
                    }
                    Handler().postDelayed({ it.isEnabled = true }, 500)
                })
            viewModel.getOrders(freights[freightsPosition].id)
        })

        viewModel.orders.observe(this, Observer {
            myLoadingListAdapter.replaceData(it)
        })

        viewModel.resultMsg.observe(this, Observer {
            ToastUtil.show(it)
        })

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}