package com.qinbang.quickrun.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.FreightBill
import com.qinbang.quickrun.ui.adapters.FreightBillListAdapter
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_freight_bill_list.*

/**
 * 运输单列表
 */
class FreightBillListActivity : AppCompatActivity() {
    companion object {
        fun goIn(context: Context) {
            context.startActivity(Intent(context, FreightBillListActivity::class.java))
        }
    }

    private val freightBillListAdapter by lazy { FreightBillListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freight_bill_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swipeRefreshLayout2.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        swipeRefreshLayout2.setOnRefreshListener {
            MainActivity2.mainViewModle.getData()
            Handler().postDelayed({ swipeRefreshLayout2.isRefreshing = false }, 300)
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab) {

            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                when (p0.position) {
                    0 -> {
                        setUnDonesData()
                    }
                    1 -> {
                        setDonesData()
                    }
                }
            }
        })

        freightBillListAdapter.setEmptyView(R.layout.layout_list_empty_view, recyclerView6)
        freightBillListAdapter.isFirstOnly(false)
        freightBillListAdapter.openLoadAnimation()
        freightBillListAdapter.setOnItemClickListener { adapter, view, position ->
            val freightBill = adapter.data[position] as FreightBill
            when (freightBill.state) {
                -1, 0 -> LoadingListActivity.goIn(this, position)
                1 -> TransportRouteActivity.goIn(this, position)
                2 -> ToastUtil.show(this, "这个货运单已删除")
                3 -> DeliveryOrderDetailActivity.goIn(this, freightBill.id, freightBill.num)
            }
        }
        recyclerView6.adapter = freightBillListAdapter
        recyclerView6.addItemDecoration(LinearSpacesItemDecoration())

        MainActivity2.mainViewModle.freightBillUnDone.observe(this, Observer {
            if (tabLayout.selectedTabPosition == 0) {
                setUnDonesData()
            }
        })
        MainActivity2.mainViewModle.freightBillDone.observe(this, Observer {
            if (tabLayout.selectedTabPosition == 1) {
                setDonesData()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDonesData() {
        val freightBillDones = MainActivity2.mainViewModle.freightBillDone.value
        if (freightBillDones != null) {
            freightBillListAdapter.replaceData(freightBillDones)
        }
    }

    private fun setUnDonesData() {
        val freightBillUnDones = MainActivity2.mainViewModle.freightBillUnDone.value
        if (freightBillUnDones != null)
            freightBillListAdapter.replaceData(freightBillUnDones)
    }
}
