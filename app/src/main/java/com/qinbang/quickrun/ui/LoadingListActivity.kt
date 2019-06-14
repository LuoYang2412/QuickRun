package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.utils.Constants
import com.qinbang.quickrun.viewmodels.LoadingListViewModel
import kotlinx.android.synthetic.main.activity_loading_list.*

/**
 * 装货清单
 */
class LoadingListActivity : AppCompatActivity() {

    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, LoadingListActivity::class.java))
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(LoadingListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_list)

        recyclerView2.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView2.addItemDecoration(LinearSpacesItemDecoration(24))
        val mAdapter = MyLoadingListAdapter()
        recyclerView2.adapter = mAdapter

        viewModel.orders.observe(this, Observer {
            mAdapter.data = it
        })
        viewModel.activeWayBill.observe(this, Observer {
            viewModel.getOrders(it.id)
            textView11.text = "货运单号：".plus(it.num)
            mAdapter.showSubBtn = it.state == 0
        })
        viewModel.netResult.observe(this, Observer {
            if (it.success) {
                if (it.api == Constants.SET_WAYBILL_OR_ORDER_STATUS) {
                    viewModel.getActWayBill()
                    findViewById<Button>(R.id.loading_sub_btn).isEnabled = true
                }
            } else {
                if (it.api == Constants.SET_WAYBILL_OR_ORDER_STATUS) {
                    findViewById<Button>(R.id.loading_sub_btn).isEnabled = true
                }
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.getActWayBill()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.loading_sub_btn -> {
                view.isEnabled = false
                viewModel.setWaybillOrOrderStatus(viewModel.activeWayBill.value!!.id)
            }
        }
    }
}

class MyLoadingListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var showSubBtn = false
    var data = ArrayList<Order>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val TYPE_FOOTER_VIEW = 1

    override fun getItemViewType(position: Int): Int {
        if (position == data.size && showSubBtn) {
            return TYPE_FOOTER_VIEW
        } else {
            return super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_FOOTER_VIEW) {
            val button = Button(parent.context)
            button.text = "完成"
            button.id = R.id.loading_sub_btn
            val layoutParams =
                RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            layoutParams.setMargins(8, 8, 8, 8)
            button.layoutParams = layoutParams
            return MyFooterHolder(button)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_loading_list_item, parent, false)
            return MyViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        if (showSubBtn) {
            return data.size + 1
        } else {
            return data.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.orderId.text = "订单号：".plus(data[position].id)
            holder.adress.text = "提货点：".plus(data[position].adress)
            var statusString =
                when (data[position].shipState) {
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
            holder.status.text = statusString
//            holder.mCheckBox.isChecked = data[position].haveOutbound
//            holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
//                data[position].haveOutbound = isChecked
//            }
        } else if (holder is MyFooterHolder) {
            holder.mBotton.setOnClickListener {
                (it.context as LoadingListActivity).onClick(it)
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId by lazy { itemView.findViewById<TextView>(R.id.textView12) }
        val adress by lazy { itemView.findViewById<TextView>(R.id.textView13) }
        val mCheckBox by lazy { itemView.findViewById<CheckBox>(R.id.checkBox) }
        val status by lazy { itemView.findViewById<TextView>(R.id.textView2) }
    }

    inner class MyFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBotton by lazy { itemView as Button }
    }
}