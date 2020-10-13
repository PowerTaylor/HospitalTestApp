package com.example.hospitaltestapp

import android.app.Application
import com.example.core.modules.coreModule
import com.example.hospitals.modules.hospitalModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HospitalApp : Application() {

    private val listOfModules = listOf(
        coreModule,
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
