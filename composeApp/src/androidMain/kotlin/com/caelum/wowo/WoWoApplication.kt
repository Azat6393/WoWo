package com.caelum.wowo

import android.app.Application
import di.KoinInitializer

class WoWoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer(this).init()
    }
}