package com.qinbang.quickrun.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.qinbang.quickrun.R
import com.qinbang.quickrun.data.model.Waybill
import com.qinbang.quickrun.utils.GlideImageLoaderForBanner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        lateinit var deliveryManViewModle: DeliveryManViewModle
    }

    private val viewModle by lazy { MainViewModle() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        deliveryManViewModle = ViewModelProviders.of(this).get(DeliveryManViewModle::class.java)

        if (deliveryManViewModle.data.value == null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //用户信息更新
        deliveryManViewModle.data.observe(this, Observer {
            if (it != null) {
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.name).text = it.name
                nav_view.getHeaderView(0).findViewById<TextView>(R.id.mobileNo).text = it.mobileNo
            }
        })

        //设置Banner
        val arrayList = ArrayList<Int>()
        arrayList.add(R.drawable.ic_menu_camera)
        arrayList.add(R.drawable.ic_menu_send)
        arrayList.add(R.drawable.ic_menu_manage)
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR)
            .setImageLoader(GlideImageLoaderForBanner())
            .setImages(arrayList)
            .setBannerAnimation(Transformer.DepthPage)
            .isAutoPlay(true)
            .setDelayTime(1500)
            .setOnBannerListener {
                Log.d("tag========", "$it")
            }
            .start()

        //设置ListView
        val myAdapter = MyAdapter()
        listView.adapter = myAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            DeliveryManDetailActivity.goIn(this, myAdapter.getData().get(position).id)
        }
        viewModle.waybillLiveData.observe(this, Observer {
            myAdapter.setData(it)
        })

        viewModle.getWaybillData("")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
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
        menuInflater.inflate(R.menu.main, menu)
        return true
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
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

class MyAdapter : BaseAdapter() {
    private val data = ArrayList<Waybill>()

    fun setData(data: ArrayList<Waybill>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun getData(): ArrayList<Waybill> {
        return data
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.layout_delivery_task_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.name.text = data.get(position).way
        viewHolder.id.text = data.get(position).id
        viewHolder.time.text = data.get(position).time
        return view
    }

    override fun getItem(position: Int): Any {
        return data.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    private class ViewHolder(view: View) {
        val name by lazy { view.findViewById<TextView>(R.id.textView2) }
        val id by lazy { view.findViewById<TextView>(R.id.textView3) }
        val time by lazy { view.findViewById<TextView>(R.id.textView4) }
    }
}