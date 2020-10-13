package com.example.hospitaltestapp

import android.app.Application
import com.example.hospitals.modules.hospitalModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HospitalApp : Application() {

    private val listOfModules = listOf(
        hospitalModule
    )

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HospitalApp)
            modules(listOfModules)
        }
    }
}
