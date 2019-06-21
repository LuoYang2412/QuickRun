package com.qinbang.quickrun.ui

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Station
import com.qinbang.quickrun.ui.adapters.DeliveryOrderListAdapter
import com.qinbang.quickrun.ui.adapters.StationListAdapter
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.utils.AlertDialogUtil
import com.qinbang.quickrun.utils.ToastUtil
import com.qinbang.quickrun.viewmodels.TransportRouteViewModel
import kotlinx.android.synthetic.main.activity_transport_route.*

/**
 * 送货路线
 */
class TransportRouteActivity : AppCompatActivity() {
    companion object {
        fun goIn(context: Context, position: Int) {
            val intent = Intent(context, TransportRouteActivity::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

    val viewModel by lazy { ViewModelProviders.of(this).get(TransportRouteViewModel::class.java) }
    lateinit var freightOrderNum: String
    lateinit var freightOrderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transport_route)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val freightsPosition = intent.getIntExtra("position", 0)
        freightOrderId = MainActivity.mainViewModle.freightBillUnDone.value?.get(freightsPosition)?.id ?: ""
        freightOrderNum = MainActivity.mainViewModle.freightBillUnDone.value?.get(freightsPosition)?.num ?: ""

        val orderListAdapter = DeliveryOrderListAdapter()
        orderListAdapter.isFirstOnly(false)
        orderListAdapter.openLoadAnimation()
        orderListAdapter.setEmptyView(R.layout.layout_list_empty_view, recyclerView4)
        orderListAdapter.setOnItemChildClickListener { adapter, view, position ->
            val deliveryOrder = (adapter as DeliveryOrderListAdapter).data[position]
            val shipmentNumber = deliveryOrder.shipmentNumber ?: ""
            val id = deliveryOrder.id ?: ""
            LossReportActivity.goIn(this, id, shipmentNumber)
        }
        recyclerView4.adapter = orderListAdapter
        recyclerView4.addItemDecoration(LinearSpacesItemDecoration(24))

        val stationListAdapter = StationListAdapter()
        stationListAdapter.setOnItemClickListener { adapter, view, position ->
            val station = adapter.data[position] as Station
            textView21.text = station.name
            orderListAdapter.replaceData(station.deliveryOrders)
            when {
                //当前站点，添加“确认送达”按钮
                station.status == Station.StationStatus.ING -> {
                    orderListAdapter.showLossReportBtn = true
                    if (orderListAdapter.footerLayoutCount == 0) {
                        val footerView = layoutInflater.inflate(R.layout.layout_radius_btn, recyclerView4, false)
                        orderListAdapter.addFooterView(footerView)
                    }
                    orderListAdapter.footerLayout.findViewById<TextView>(R.id.radius_btn_content_textView14).text =
                        "确认送达"
                    orderListAdapter.footerLayout.findViewById<CardView>(R.id.radius_btn_cardView).setOnClickListener {
                        it.isEnabled = false
                        viewModel.setWaybillOrOrderStatus(
                            freightOrderId,
                            station.id
                        )
                        Handler().postDelayed({ it.isEnabled = true }, 500)
                    }
                    //站点没有订单需要卸载，自动点击“确认送达”
                    if (station.deliveryOrders.size == 0) {
                        orderListAdapter.footerLayout.findViewById<CardView>(R.id.radius_btn_cardView).performClick()
                    }
                }
                //不是当前站点，异常“确认送达”按钮
                (station.status == Station.StationStatus.ED || station.status == Station.StationStatus.WILL) && orderListAdapter.footerLayoutCount == 1 -> {
                    orderListAdapter.showLossReportBtn = false
                    orderListAdapter.removeAllFooterView()
                }
            }
        }
        recyclerView3.adapter = stationListAdapter

        MainActivity.mainViewModle.freightBillUnDone.observe(this, Observer {
            viewModel.getData(freightOrderId)
        })

        viewModel.getDataSuccess.observe(this, Observer {
            //获取数据成功，组装数据
            if (it[0] && it[1]) {
                viewModel.packageData()
            }
        })

        viewModel.stationsLiveData.observe(this, Observer {
            if (viewModel.routesDone) {
                AlertDialogUtil.show(this, "物流单已全部送达", "确定", DialogInterface.OnClickListener { dialog, which ->
                    finish()
                })
                return@Observer
            }
            stationListAdapter.replaceData(it)
            Handler().postDelayed({
                //滚动到当前站点
                (recyclerView3.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                    viewModel.ingStationPosition,
                    0
                )
                //点击当前站点
                recyclerView3.findViewHolderForAdapterPosition(viewModel.ingStationPosition)?.itemView?.performClick()
            }, 200)
        })

        viewModel.resultMsg.observe(this, Observer {
            ToastUtil.show(this, it)
        })

    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.dragFloatActionButton -> {
                RiskReportingActivity.goIn(this, freightOrderId, freightOrderNum)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}