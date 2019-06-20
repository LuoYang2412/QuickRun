package com.qinbang.quickrun.ui.adapters

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.FreightBill

class FreightBillListAdapter : BaseQuickAdapter<FreightBill, BaseViewHolder>(R.layout.layout_delivery_task_item) {
    override fun convert(helper: BaseViewHolder, item: FreightBill) {
        helper.setText(R.id.textView3, "货运单号:".plus(item.num))
            .setText(R.id.textView27, item.route)
            .setText(
                R.id.textView26, when (item.state) {
                    -1 -> "待出库"
                    0 -> "待配送"
                    1 -> "配送中"
                    2 -> "删除"
                    3 -> "已完成"
                    else -> ""
                }
            )
            .setText(R.id.textView4, item.createDate)
    }
}