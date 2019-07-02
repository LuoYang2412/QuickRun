package com.luoyang.quickrun.ui

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.luoyang.quickrun.R
import com.luoyang.quickrun.data.model.FreightBill
import com.luoyang.quickrun.net.ServiceCreator
import com.luoyang.quickrun.ui.adapters.FreightBillListAdapter
import com.luoyang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.luoyang.quickrun.utils.GlideImageLoaderForBanner
import com.luoyang.quickrun.utils.ToastUtil
import com.luoyang.quickrun.viewmodels.MainViewModle
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_freight_bill_list.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber

class MainActivity2 : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        lateinit var mainViewModle: MainViewModle
    }

    private val freightBillListAdapter by lazy { FreightBillListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

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
        navView.itemIconTintList = null
        navView.setNavigationItemSelectedListener(this)

        //设置Banner
        val arrayList = ArrayList<String>()
        arrayList.add("https://api.neweb.top/bing.php?type=future")
        arrayList.add("https://api.neweb.top/bing.php?type=rand")
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            .setImageLoader(GlideImageLoaderForBanner())
            .setImages(arrayList)
            .setBannerAnimation(Transformer.DepthPage)
            .isAutoPlay(true)
            .setDelayTime(1500)
            .setOnBannerListener {
                Timber.d("banner点击$it")
            }
            .start()

        swipeRefreshLayout2.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        swipeRefreshLayout2.setOnRefreshListener {
            if (mainViewModle.logined()) {
                mainViewModle.getData()
            }
            Handler().postDelayed({ swipeRefreshLayout2.isRefreshing = false }, 500)
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
                2 -> ToastUtil.show("这个货运单已删除")
                3 -> DeliveryOrderDetailActivity.goIn(this, freightBill.id, freightBill.num)
            }
        }
        recyclerView6.adapter = freightBillListAdapter
        recyclerView6.addItemDecoration(LinearSpacesItemDecoration())

        mainViewModle = ViewModelProviders.of(this).get(MainViewModle::class.java)
        mainViewModle.logined()
        //用户信息
        mainViewModle.driver.observe(this, Observer {
            if (it != null) {
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.name).text = it.realName
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.mobileNo).text = it.mobilePhone
                val userIcon = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load("${ServiceCreator.IMAGE_BASE_URL}${it.image[0]}")
                    .apply(RequestOptions.circleCropTransform()).into(userIcon)

                mainViewModle.getData()
            } else {
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.name).text = ""
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.mobileNo).text = ""
                val userIcon = nav_view.getHeaderView(0).findViewById<ImageView>(R.id.imageView)
                Glide.with(this).load(R.mipmap.ic_launcher_round)
                    .apply(RequestOptions.circleCropTransform()).into(userIcon)
            }
        })

        mainViewModle.freightBillUnDone.observe(this, Observer {
            if (tabLayout.selectedTabPosition == 0) {
                setUnDonesData()
            }
        })
        mainViewModle.freightBillDone.observe(this, Observer {
            if (tabLayout.selectedTabPosition == 1) {
                setDonesData()
            }
        })
        //错误信息
        mainViewModle.errorMsg.observe(this, Observer {
            when {
                it == null -> return@Observer
                it == "未登录" -> LoginActivity.goIn(this)
                else -> Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_reset_phone -> {
                if (mainViewModle.logined()) {
                    ResetPhoneActivity.goIn(this)
                }
            }
            R.id.nav_reset_password -> {
                if (mainViewModle.logined()) {
                    ResetPasswordActivity.goIn(this)
                }
            }
            R.id.nav_out_login -> {
                if (mainViewModle.logined()) {
                    MainActivity2.mainViewModle.loginOut()
                }
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setDonesData() {
        val freightBillDones = mainViewModle.freightBillDone.value
        if (freightBillDones != null) {
            freightBillListAdapter.replaceData(freightBillDones)
        }
    }

    private fun setUnDonesData() {
        val freightBillUnDones = mainViewModle.freightBillUnDone.value
        if (freightBillUnDones != null)
            freightBillListAdapter.replaceData(freightBillUnDones)
    }

    override fun onStart() {
        super.onStart()
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        banner.stopAutoPlay()
    }
}
