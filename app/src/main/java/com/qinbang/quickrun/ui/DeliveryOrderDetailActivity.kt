package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import kotlinx.android.synthetic.main.activity_delivery_order_detail.*

/**
 * 货单详情
 */
class DeliveryOrderDetailActivity : AppCompatActivity() {

    companion object {
        fun goIn(context: Context, id: String) {
            val intent = Intent(context, DeliveryOrderDetailActivity::class.java)
            intent.putExtra("id", id)
            context.startActivity(intent)
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(DeliveryOrderDetailViewModel::class.java) }
    lateinit var deliveryOrderListAdapter: DeliveryOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_order_detail)
        textView.text = intent.getStringExtra("id")

        recyclerView.addItemDecoration(LinearSpacesItemDecoration(24))
        deliveryOrderListAdapter = DeliveryOrderListAdapter()
        recyclerView.adapter = deliveryOrderListAdapter
    }
}

class DeliveryOrderListAdapter : RecyclerView.Adapter<DeliveryOrderListAdapter.DeliveryOrderListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryOrderListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_delivery_order_detail_list_item, parent, false)
        return DeliveryOrderListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: DeliveryOrderListViewHolder, position: Int) {

    }

    inner class DeliveryOrderListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}