package com.qinbang.quickrun.ui.adapters

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Station

class StationListAdapter : BaseQuickAdapter<Station, BaseViewHolder>(
    R.layout.layout_station_item
) {
    override fun convert(helper: BaseViewHolder?, item: Station?) {
        val stationStatusIcon = helper?.getView<ImageView>(R.id.imageView12)
        val ingIcon = helper?.getView<ImageView>(R.id.imageView14)
        when (item!!.status) {
            Station.StationStatus.WILL -> {
//                Glide.with(mContext).load(R.drawable.nonono).into(stationStatusIcon!!)
                stationStatusIcon?.setImageResource(R.drawable.nonono)
                ingIcon!!.visibility = View.INVISIBLE
            }
            Station.StationStatus.ING -> {
//                Glide.with(mContext).load(R.drawable.inging).into(stationStatusIcon!!)
                stationStatusIcon?.setImageResource(R.drawable.inging)
                ingIcon!!.visibility = View.VISIBLE
            }
            Station.StationStatus.ED -> {
//                Glide.with(mContext).load(R.drawable.okokok).into(stationStatusIcon!!)
                stationStatusIcon?.setImageResource(R.drawable.okokok)
                ingIcon!!.visibility = View.INVISIBLE
            }
        }
        helper.setText(R.id.textView22, item.name)
    }
}