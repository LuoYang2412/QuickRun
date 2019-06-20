package com.qinbang.quickrun

import android.app.Application
import timber.log.Timber


class QuickRunApplication : Application() {

    companion object {
        lateinit var application: QuickRunApplication
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initTools()
    }

    private fun initTools() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}