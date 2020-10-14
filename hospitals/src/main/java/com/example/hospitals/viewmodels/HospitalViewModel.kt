package com.example.hospitals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.repositories.HospitalDataRepository
import com.example.hospitals.models.HospitalItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

data class HospitalViewState(
    val hospitalItemModel: HospitalItemModel? = null,
    val showError: Boolean = false
)

class HospitalViewModel(
    private val hospitalDataRepository: HospitalDataRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<HospitalViewState>()
    val viewState: LiveData<HospitalViewState> = _viewState

    private val compositeDisposable = CompositeDisposable()

    fun initialise(hospitalId: Long) {
        getHospital(hospitalId = hospitalId)
    }

    private fun getHospital(hospitalId: Long) {
        compositeDisposable += hospitalDataRepository.getHospital(hospitalId = hospitalId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    _viewState.value = currentViewState().copy(showError = true)
                },
                onSuccess = {
                    _viewState.value = currentViewState()
                        .copy(
                            hospitalItemModel = it.mapToHospitalItemModel(),
                            showError = false
                        )
                }
            )
    }

    private fun currentViewState(): HospitalViewState {
        return viewState.value ?: HospitalViewState()
    }

    private fun HospitalsDataModel.mapToHospitalItemModel(): HospitalItemModel {
        return HospitalItemModel(
            name = name,
            addressLine1 = address1,
            addressLine2 = address2,
            addressLine3 = address3,
            city = city,
            postCode = postCode,
            number = phone,
            website = website,
            sector = sector
        )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
