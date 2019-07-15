package com.luoyang.quickrun.ui.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luoyang.quickrun.R
import com.luoyang.quickrun.data.model.DeliveryOrder

class DeliveryOrderListAdapter :
    BaseQuickAdapter<DeliveryOrder, BaseViewHolder>(R.layout.layout_delivery_order_detail_list_item) {
    var showLossReportBtn = false
    override fun convert(helper: BaseViewHolder, item: DeliveryOrder) {
        helper.setText(R.id.textView38, item.shipmentNumber)
            .setText(R.id.textView40, item.adress)
            .setText(
                R.id.textView44,
                when (item.shipState) {
                    0 -> "待拣货"
                    1 -> "已拣货"
                    3 -> "待出库"
                    4 -> "待配送"
                    5 -> "配送中"
                    6 -> "已完成"
                    else -> ""
                }
            )
            .setGone(R.id.button4, showLossReportBtn)
            .addOnClickListener(R.id.button4)
    }
}