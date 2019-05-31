package com.qinbang.quickrun

import android.app.Application

class QuickRunApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
    }

    companion object {
        lateinit var application: QuickRunApplication
    }
}