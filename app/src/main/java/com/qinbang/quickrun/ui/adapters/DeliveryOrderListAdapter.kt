package com.qinbang.quickrun.ui.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.DeliveryOrder

class DeliveryOrderListAdapter :
    BaseQuickAdapter<DeliveryOrder, BaseViewHolder>(R.layout.layout_delivery_order_detail_list_item) {
    var showLossReportBtn = false
    override fun convert(helper: BaseViewHolder, item: DeliveryOrder) {
        helper.setText(R.id.textView38, item.shipmentNumber)
            .setText(R.id.textView40, item.adress)
            .setText(
                R.id.textView44,
                when (item.shipState) {
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
            )
            .setGone(R.id.button4, showLossReportBtn)
            .addOnClickListener(R.id.button4)
    }
}