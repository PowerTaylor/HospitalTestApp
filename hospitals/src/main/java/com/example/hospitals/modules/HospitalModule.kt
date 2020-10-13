package com.example.hospitals.modules

import com.example.hospitals.viewmodels.HospitalListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val hospitalModule = module {
    viewModel { HospitalListViewModel(get()) }
}
