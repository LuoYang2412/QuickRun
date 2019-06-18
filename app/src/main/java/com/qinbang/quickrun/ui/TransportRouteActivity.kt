package com.qinbang.quickrun.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Order
import com.qinbang.quickrun.data.model.Station
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.utils.Constants
import com.qinbang.quickrun.viewmodels.LoadingListViewModel
import com.qinbang.quickrun.viewmodels.TransportRouteViewModel
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
    val loadingListViewModel by lazy { ViewModelProviders.of(this).get(LoadingListViewModel::class.java) }
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

        loadingListViewModel.getActWayBill()//获取货运订单
        loadingListViewModel.activeWayBill.observe(this, Observer {
            //获取货运单成功，获取路线
            viewModel.getRoute(loadingListViewModel.activeWayBill.value!!.id)
        })
        viewModel.stationsLiveData.observe(this, Observer {
            stationListAdapter.data = it
            //获取路线成功，获取订单
            loadingListViewModel.getOrders(loadingListViewModel.activeWayBill.value!!.id)
        })
        loadingListViewModel.orders.observe(this, Observer {
            //获取订单成功，组装数据显示
            var shipStateDone = true
            it.map { order ->
                shipStateDone = order.shipState == 7 && shipStateDone
                if (shipStateDone) {
                    AlertDialog.Builder(this)
                        .setMessage("物流单已全部送达")
                        .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                            finish()
                        }).create().show()
                }
                viewModel.stationsLiveData.value!!.map { station ->
                    if (order.pickUpId == station.id) {
                        if (station.orders == null) {
                            station.orders = ArrayList<Order>()
                        }
                        station.orders.add(order)
                    }
                }
            }
            var index = 0
            if (viewModel.currentStation.value!! < viewModel.stationsLiveData.value!!.size && viewModel.currentStation.value!! > 0) {
                index = viewModel.currentStation.value!! - 1
            } else if (viewModel.currentStation.value!! > viewModel.stationsLiveData.value!!.size) {
                index = viewModel.stationsLiveData.value!!.size - 1
            }
            if (viewModel.stationsLiveData.value!![index].orders == null) {
                loadingListViewModel.setWaybillOrOrderStatus(
                    loadingListViewModel.activeWayBill.value!!.id,
                    viewModel.stationsLiveData.value!![index].id
                )
            }
            (recyclerView4.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                index,
                0
            )
            stationOrderListAdapter.showSubBtn =
                viewModel.stationsLiveData.value!![index].status == Station.StationStatus.ING
            stationOrderListAdapter.data =
                viewModel.stationsLiveData.value!![index].orders ?: ArrayList<Order>()
            textView21.text = stationListAdapter.data[index].name
        })
        viewModel.currentStation.observe(this, Observer { index ->
            //站点变化，修改站点状态
            viewModel.stationsLiveData.value!!.map {
                if ((it.sort.toInt()) < index) {
                    it.status = Station.StationStatus.ED
                } else if ((it.sort.toInt()) == index) {
                    it.status = Station.StationStatus.ING
                } else {
                    it.status = Station.StationStatus.WILL
                }
            }
        })
        viewModel.netResult.observe(this, Observer {
            //网络请求返回
            if (it.success) {
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        loadingListViewModel.netResult.observe(this, Observer {
            if (it.success) {
                if (it.api == Constants.SET_WAYBILL_OR_ORDER_STATUS) {
                    viewModel.getRoute(loadingListViewModel.activeWayBill.value!!.id)
                    findViewById<Button>(R.id.loading_sub_btn).isEnabled = true
                }
            } else {
                if (it.api == Constants.SET_WAYBILL_OR_ORDER_STATUS) {
                    findViewById<Button>(R.id.loading_sub_btn).isEnabled = true
                } else if (it.api == Constants.GET_ACT_WAY_BILL) {
                    if (it.message.contains("Size")) {
                        AlertDialog.Builder(this)
                            .setMessage("暂无路线信息")
                            .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                                finish()
                            })
                            .create()
                            .show()
                    }
                }
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.constraintLayout1 -> {
                val index = view.tag as Int
                val orders = stationListAdapter.data[index].orders
                stationOrderListAdapter.showSubBtn =
                    viewModel.stationsLiveData.value!![index].status == Station.StationStatus.ING
                stationOrderListAdapter.data = orders ?: ArrayList<Order>()
                textView21.text = stationListAdapter.data[index].name
            }
            R.id.textView25 -> {
                LossReportActivity.goIn(
                    this,
                    stationOrderListAdapter.data[view.tag as Int].id,
                    stationOrderListAdapter.data[view.tag as Int].shipmentNumber
                )
            }
            R.id.loading_sub_btn -> {
                val pickUpId = view.tag as String
                view.isEnabled = false
                loadingListViewModel.setWaybillOrOrderStatus(loadingListViewModel.activeWayBill.value!!.id, pickUpId)
            }
        }
    }
}

class StationListAdapter : RecyclerView.Adapter<StationListAdapter.StationViewHolder>() {
    var data = ArrayList<Station>()
        set(value) {
            field = value
            notifyDataSetChanged()
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

class StationOrderListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_FOOTER_VIEW = 1
    var showSubBtn = false
    var data = ArrayList<Order>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_FOOTER_VIEW) {
            val button = Button(parent.context)
            button.text = "确认送达"
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
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_station_order_item, parent, false)
            return StationOrderViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        if (showSubBtn && data.size > 0) {
            return data.size + 1
        } else {
            return data.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == data.size && showSubBtn && data.size > 0) {
            return TYPE_FOOTER_VIEW
        } else {
            return super.getItemViewType(position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyFooterHolder) {
            holder.mBotton.tag = data[0].pickUpId
            holder.mBotton.setOnClickListener {
                (it.context as TransportRouteActivity).onClick(it)
            }
        } else if (holder is StationOrderViewHolder) {
            holder.orderId.text = data[position].shipmentNumber
            holder.status.text =
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
            holder.lossReport.tag = position
        }
    }

    inner class StationOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderId by lazy { itemView.findViewById<TextView>(R.id.textView24) }
        val lossReport by lazy { itemView.findViewById<TextView>(R.id.textView25) }
        val status by lazy { itemView.findViewById<TextView>(R.id.textView45) }
    }

    inner class MyFooterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mBotton by lazy { itemView as Button }
    }
}