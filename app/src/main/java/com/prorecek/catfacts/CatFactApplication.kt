package com.prorecek.catfacts

import android.app.Application
import com.prorecek.catfacts.di.catFactModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CatFactApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CatFactApplication)
            modules(catFactModule)
        }
    }
}