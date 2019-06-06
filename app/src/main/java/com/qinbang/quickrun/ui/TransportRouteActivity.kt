package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.data.model.Station
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import kotlinx.android.synthetic.main.activity_transport_route.*

/**
 * 送货路线
 */
class TransportRouteActivity : AppCompatActivity() {
    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, TransportRouteActivity::class.java))
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(TransportRouteViewModel::class.java) }
    lateinit var stationListAdapter: StationListAdapter
    lateinit var stationOrderListAdapter: StationOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_route)

        stationListAdapter = StationListAdapter()
        recyclerView3.adapter = stationListAdapter

        stationOrderListAdapter = StationOrderListAdapter()
        recyclerView4.adapter = stationOrderListAdapter
        recyclerView4.addItemDecoration(LinearSpacesItemDecoration(24))

        viewModel.getData()
        viewModel.stationsLiveData.observe(this, Observer {
            stationListAdapter.setData(it)
            stationOrderListAdapter.data = it[0].orders
            textView21.text = stationListAdapter.getData()[0].name
        })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.constraintLayout1 -> {
                val orders = stationListAdapter.getData()[view.tag as Int].orders
                stationOrderListAdapter.data = orders
                textView21.text = stationListAdapter.getData()[view.tag as Int].name
                stationOrderListAdapter.notifyDataSetChanged()
            }
            R.id.textView25 -> {
                LossReportActivity.goIn(this, stationOrderListAdapter.data[view.tag as Int].id)
            }
        }
    }
}

class StationListAdapter : RecyclerView.Adapter<StationListAdapter.StationViewHolder>() {
    private val data = ArrayList<Station>()
    fun setData(data: ArrayList<Station>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun getData(): ArrayList<Station> {
        return data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_station_item, parent, false)
        return StationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        when (data[position].status) {
            Station.StationStatus.WILL -> {
                holder.stationStatusIcon.setImageResource(R.drawable.nonono)
                holder.ingIcon.visibility = View.INVISIBLE
            }
            Station.StationStatus.ING -> {
                holder.stationStatusIcon.setImageResource(R.drawable.inging)
                holder.ingIcon.visibility = View.VISIBLE
            }
            Station.StationStatus.ED -> {
                holder.stationStatusIcon.setImageResource(R.drawable.okokok)
                holder.ingIcon.visibility = View.INVISIBLE
            }
        }
        holder.stationName.text = data[position].name
        holder.itemView.tag = position
    }

    inner class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationStatusIcon by lazy { itemView.findViewById<ImageView>(R.id.imageView12) }
        val stationName by lazy { itemView.findViewById<TextView>(R.id.textView22) }
        val ingIcon by lazy { itemView.findViewById<ImageView>(R.id.imageView14) }
    }
}

class StationOrderListAdapter : RecyclerView.Adapter<StationOrderListAdapter.StationOrderViewHolder>() {
    var data = ArrayList<Order>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationOrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_station_order_item, parent, false)
        return StationOrderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StationOrderViewHolder, position: Int) {
        holder.orderId.text = data[position].id
        holder.lossReport.tag = position
    }

    inner class StationOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId by lazy { itemView.findViewById<TextView>(R.id.textView24) }
        val lossReport by lazy { itemView.findViewById<TextView>(R.id.textView25) }
    }
}