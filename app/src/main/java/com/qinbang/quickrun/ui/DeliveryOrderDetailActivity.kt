package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.viewmodels.DeliveryOrderDetailViewModel
import com.qinbang.quickrun.viewmodels.LoadingListViewModel
import kotlinx.android.synthetic.main.activity_delivery_order_detail.*

/**
 * 货单详情
 */
class DeliveryOrderDetailActivity : AppCompatActivity() {

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
    lateinit var deliveryOrderListAdapter: DeliveryOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_order_detail)

        textView.text = intent.getStringExtra("num")

        recyclerView.addItemDecoration(LinearSpacesItemDecoration())
        deliveryOrderListAdapter = DeliveryOrderListAdapter()
        recyclerView.adapter = deliveryOrderListAdapter

        loadingListViewModel.getOrders(intent.getStringExtra("id"))
        loadingListViewModel.orders.observe(this, Observer {
            deliveryOrderListAdapter.data = it
        })
    }
}

class DeliveryOrderListAdapter : RecyclerView.Adapter<DeliveryOrderListAdapter.DeliveryOrderListViewHolder>() {
    var data = ArrayList<Order>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryOrderListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_delivery_order_detail_list_item, parent, false)
        return DeliveryOrderListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: DeliveryOrderListViewHolder, position: Int) {
        holder.orderId.text = data[position].id
        holder.address.text = data[position].adress
        holder.status.text = when (data[position].shipState) {
            -2 -> "待拣货"
            -1 -> "已拣货"
            0 -> "待出库"
            1 -> "已出库"
            2 -> "删除"
            3 -> "待配送"
            4 -> "配送中"
            5 -> "待入库"
            6 -> "已入库"
            7 -> "已完成"
            else -> ""
        }
    }

    inner class DeliveryOrderListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId by lazy { itemView.findViewById<TextView>(R.id.textView38) }
        val address by lazy { itemView.findViewById<TextView>(R.id.textView40) }
        val status by lazy { itemView.findViewById<TextView>(R.id.textView44) }
    }
}