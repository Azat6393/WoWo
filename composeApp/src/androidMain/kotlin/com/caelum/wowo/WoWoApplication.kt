package com.caelum.wowo

import android.app.Application
import di.commonModule
import di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.game.GameViewModel

class WoWoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@WoWoApplication)
            modules(domainModule, commonModule)
            modules(
                module { viewModel { GameViewModel(get()) } }
            )
        }
    }
}