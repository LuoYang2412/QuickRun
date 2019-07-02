package com.luoyang.quickrun.ui.adapters

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.luoyang.quickrun.R
import com.luoyang.quickrun.data.model.Station

class StationListAdapter : BaseQuickAdapter<Station, BaseViewHolder>(
    R.layout.layout_station_item
) {
    override fun convert(helper: BaseViewHolder?, item: Station?) {
        val stationStatusIcon = helper?.getView<ImageView>(R.id.imageView12)
        val ingIcon = helper?.getView<ImageView>(R.id.imageView14)
        when (item!!.status) {
            Station.StationStatus.WILL -> {
//                Glide.with(mContext).load(R.drawable.ic_station_will).into(stationStatusIcon!!)
                stationStatusIcon?.setImageResource(R.drawable.ic_station_will)
                ingIcon!!.visibility = View.INVISIBLE
            }
            Station.StationStatus.ING -> {
//                Glide.with(mContext).load(R.drawable.ic_station_ing).into(stationStatusIcon!!)
                stationStatusIcon?.setImageResource(R.drawable.ic_station_ing)
                ingIcon!!.visibility = View.VISIBLE
            }
            Station.StationStatus.ED -> {
//                Glide.with(mContext).load(R.drawable.ic_station_ed).into(stationStatusIcon!!)
                stationStatusIcon?.setImageResource(R.drawable.ic_station_ed)
                ingIcon!!.visibility = View.INVISIBLE
            }
        }
        helper.setText(R.id.textView22, item.name)
    }
}