package com.luoyang.quickrun.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
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
import com.luoyang.quickrun.R
import com.luoyang.quickrun.data.model.FreightBill
import com.luoyang.quickrun.net.ServiceCreator
import com.luoyang.quickrun.ui.adapters.FreightBillListAdapter
import com.luoyang.quickrun.ui.widget.LinearSpacesItemDecoration
import com.luoyang.quickrun.utils.GlideImageLoaderForBanner
import com.luoyang.quickrun.viewmodels.MainViewModle
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        lateinit var mainViewModle: MainViewModle
    }

    val freightBillListAdapter: FreightBillListAdapter by lazy { FreightBillListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //设置Banner
        val arrayList = ArrayList<String>()
        arrayList.add("https://api.neweb.top/bing.php?type=rand")
        arrayList.add("https://api.neweb.top/bing.php?type=future")
        arrayList.add("https://api.neweb.top/bing.php")
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            .setImageLoader(GlideImageLoaderForBanner())
            .setImages(arrayList)
            .setBannerAnimation(Transformer.DepthPage)
            .isAutoPlay(true)
            .setDelayTime(3000)
            .setOnBannerListener {
                Timber.d("banner点击$it")
            }
            .start()

        //设置List
        freightBillListAdapter.isFirstOnly(false)
        freightBillListAdapter.openLoadAnimation()
        freightBillListAdapter.setEmptyView(R.layout.layout_list_empty_view, recyclerView5)
        freightBillListAdapter.setOnItemClickListener { adapter, view, position ->
            val freightBill = adapter.data[position] as FreightBill
            DeliveryOrderDetailActivity.goIn(
                this,
                freightBill.id,
                freightBill.outputNum
            )
        }
        recyclerView5.adapter = freightBillListAdapter
        recyclerView5.addItemDecoration(LinearSpacesItemDecoration())

        swipeRefreshLayout1.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN)
        swipeRefreshLayout1.setOnRefreshListener {
            mainViewModle.getData()
            Handler().postDelayed({ swipeRefreshLayout1.isRefreshing = false }, 500)
        }

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

        mainViewModle = ViewModelProviders.of(this).get(MainViewModle::class.java)
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
                startActivity(Intent(this, LoginActivity::class.java))
            }
        })
        //历史运单
        mainViewModle.freightBillDone.observe(this, Observer {
            freightBillListAdapter.replaceData(it)
        })
        //错误信息
        mainViewModle.errorMsg.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
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
            R.id.imageView2, R.id.imageView3 -> {
                FreightBillListActivity.goIn(this)
            }
            R.id.imageView4 -> {
                LossReportActivity.goIn(this)
            }
            R.id.imageView5 -> {
                RiskReportingActivity.goIn(this, "", "")
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_reset_phone -> {
                ResetPhoneActivity.goIn(this)
            }
            R.id.nav_reset_password -> {
                ResetPasswordActivity.goIn(this)
            }
            R.id.nav_out_login -> {
                mainViewModle.loginOut()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}