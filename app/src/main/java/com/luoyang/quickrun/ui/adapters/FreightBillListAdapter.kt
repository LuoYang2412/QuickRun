package com.luoyang.quickrun.ui.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luoyang.quickrun.R
import com.luoyang.quickrun.data.model.FreightBill

class FreightBillListAdapter : BaseQuickAdapter<FreightBill, BaseViewHolder>(R.layout.layout_delivery_task_item) {
    override fun convert(helper: BaseViewHolder, item: FreightBill) {
        helper.setText(R.id.textView3, "出库单号:".plus(item.outputNum))
            .setText(R.id.textView27, item.route)
            .setText(
                R.id.textView26, when (item.state) {
                    0 -> "待配送"
                    1 -> "配送中"
                    3 -> "已完成"
                    else -> ""
                }
            )
            .setText(R.id.textView4, item.createDate)
    }
}