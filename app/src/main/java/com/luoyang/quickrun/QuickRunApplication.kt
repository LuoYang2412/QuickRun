package com.luoyang.quickrun

import android.app.Activity
import android.app.Application
import android.os.Process
import timber.log.Timber


class QuickRunApplication : Application() {
    private val activitys = ArrayList<Activity>()

    companion object {
        lateinit var application: QuickRunApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initTools()
    }

    fun addActivity(activity: Activity) {
        activitys.add(activity)
    }

    fun removeTopActivity() {
        activitys.removeAt(activitys.size - 1)
    }

    fun exit() {
        activitys.map {
            it.finish()
        }
        activitys.clear()
        Process.killProcess(Process.myPid())
    }

    private fun initTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}