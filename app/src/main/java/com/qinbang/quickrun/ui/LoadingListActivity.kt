package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
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

        viewModel.getData()
        viewModel.orders.observe(this, Observer {
            mAdapter.setData(it)
        })

    }

    fun onClick(view: View) {
        when (view) {
            findViewById<Button>(R.id.loading_sub_btn) -> {
                Log.d("tag=========", "点击")
            }
        }
    }
}

class MyLoadingListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data = ArrayList<Order>()
    private val TYPE_FOOTER_VIEW = 1;

    fun setData(data: ArrayList<Order>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun getData(): ArrayList<Order> {
        return data
    }

    override fun getItemViewType(position: Int): Int {
        if (position == data.size) {
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
        return data.size + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.orderId.text = data[position].id
            holder.mCheckBox.isChecked = data[position].haveOutbound
            holder.mCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
                data[position].haveOutbound = isChecked
            }
        } else if (holder is MyFooterHolder) {
            holder.mBotton.setOnClickListener {
                (it.context as LoadingListActivity).onClick(it)
            }
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId by lazy { itemView.findViewById<TextView>(R.id.textView13) }
        val mCheckBox by lazy { itemView.findViewById<CheckBox>(R.id.checkBox) }
    }

    inner class MyFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBotton by lazy { itemView as Button }
    }
}