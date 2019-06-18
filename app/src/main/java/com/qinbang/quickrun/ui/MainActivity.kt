package com.qinbang.quickrun.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Waybill
import com.qinbang.quickrun.net.ServiceCreator
import com.qinbang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.qinbang.quickrun.utils.GlideImageLoaderForBanner
import com.qinbang.quickrun.viewmodels.MainViewModle
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        lateinit var mainViewModle: MainViewModle
    }

    private lateinit var historyTastListAdapter: HistoryTastListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModle = ViewModelProviders.of(this).get(MainViewModle::class.java)

        if (mainViewModle.deliveryManData.value == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            mainViewModle.getWaybillData()
        }

        //用户信息更新
        mainViewModle.deliveryManData.observe(this, Observer {
            if (it != null) {
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.name).text = it.realName
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.mobileNo).text = it.mobilePhone
                val userIcon = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load("${ServiceCreator.IMAGE_BASE_URL}${it.image[0]}")
                    .apply(RequestOptions.circleCropTransform()).into(userIcon)

                mainViewModle.getWaybillData()
            } else {
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.name).text = ""
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.mobileNo).text = ""
                val userIcon = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load(R.mipmap.ic_launcher_round)
                    .apply(RequestOptions.circleCropTransform()).into(userIcon)

                mainViewModle.getWaybillData()
            }
        })

        //设置Banner
        val arrayList = ArrayList<Int>()
        arrayList.add(R.drawable.ic_menu_camera)
        arrayList.add(R.drawable.ic_menu_send)
        arrayList.add(R.drawable.ic_menu_manage)
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            .setImageLoader(GlideImageLoaderForBanner())
            .setImages(arrayList)
            .setBannerAnimation(Transformer.DepthPage)
            .isAutoPlay(true)
            .setDelayTime(1500)
            .setOnBannerListener {
                Log.d("tag========", "$it")
            }
            .start()

        //设置List
        historyTastListAdapter = HistoryTastListAdapter()
        recyclerView5.adapter = historyTastListAdapter
        recyclerView5.addItemDecoration(LinearSpacesItemDecoration())

        mainViewModle.waybillLiveData.observe(this, Observer {
            historyTastListAdapter.data = it
        })
        mainViewModle.netResult.observe(this, Observer {
            if (it.success) {
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner.stopAutoPlay()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.imageView2 -> {
                LoadingListActivity.goIn(this)
            }
            R.id.imageView3 -> {
                TransportRouteActivity.goIn(this)
            }
            R.id.imageView4 -> {
                LossReportActivity.goIn(this)
            }
            R.id.imageView5 -> {
                RiskReportingActivity.goIn(this)
            }
            R.id.cardView8 -> {
                DeliveryOrderDetailActivity.goIn(
                    this,
                    historyTastListAdapter.data[view.tag as Int].id,
                    historyTastListAdapter.data[view.tag as Int].num
                )
            }
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_phone_edit -> {
                MobilePhoneChangeActivity.goIn(this)
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_out_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

class HistoryTastListAdapter : RecyclerView.Adapter<HistoryTastListAdapter.HistoryTaskViewHolder>() {
    var data = ArrayList<Waybill>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryTaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_delivery_task_item, parent, false)
        return HistoryTaskViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HistoryTaskViewHolder, position: Int) {
        holder.id.text = "货运单号:".plus(data.get(position).num)
        holder.route.text = data.get(position).route
        holder.time.text = data.get(position).createDate
        holder.itemView.tag = position
        var statusString =
            when (data[position].state) {
                0 -> "待配送"
                1 -> "配送中"
                2 -> "删除"
                3 -> "已完成"
                else -> ""
            }
        holder.state.text = statusString
    }


    inner class HistoryTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id by lazy { itemView.findViewById<TextView>(R.id.textView3) }
        val route by lazy { itemView.findViewById<TextView>(R.id.textView27) }
        val state by lazy { itemView.findViewById<TextView>(R.id.textView26) }
        val time by lazy { itemView.findViewById<TextView>(R.id.textView4) }
    }
}