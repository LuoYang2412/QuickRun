package com.luoyang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.luoyang.quickrun.R
import com.luoyang.quickrun.ui.adapters.DeliveryOrderListAdapter
import com.luoyang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.luoyang.quickrun.viewmodels.DeliveryOrderDetailViewModel
import com.luoyang.quickrun.viewmodels.LoadingListViewModel
import kotlinx.android.synthetic.main.activity_delivery_order_detail.*

/**
 * 货单详情
 */
class DeliveryOrderDetailActivity : BaseActivity() {

    companion object {
        fun goIn(context: Context, id: String, num: String) {
            val intent = Intent(context, DeliveryOrderDetailActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("num", num)
            context.startActivity(intent)
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(DeliveryOrderDetailViewModel::class.java) }
    val loadingListViewModel by lazy { ViewModelProviders.of(this).get(LoadingListViewModel::class.java) }
    val deliveryOrderListAdapter by lazy { DeliveryOrderListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_order_detail)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView.text = intent.getStringExtra("num")

        recyclerView.addItemDecoration(LinearSpacesItemDecoration())
        recyclerView.adapter = deliveryOrderListAdapter

        loadingListViewModel.getOrders(intent.getStringExtra("id"))
        loadingListViewModel.orders.observe(this, Observer {
            deliveryOrderListAdapter.replaceData(it)
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}