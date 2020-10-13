package com.example.core.modules

import com.example.core.hospitaldata.repositories.HospitalDataRepository
import com.example.core.hospitaldata.repositories.HospitalDataRepositoryImpl
import org.koin.dsl.module

val coreModule = module {
    single<HospitalDataRepository> { HospitalDataRepositoryImpl(get()) }
}
