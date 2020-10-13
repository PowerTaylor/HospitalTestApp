package com.example.hospitals.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.hospitaldata.models.HospitalsDataModel
import com.example.core.hospitaldata.repositories.HospitalDataRepository
import com.example.core.hospitaldata.repositories.HospitalFilterOptions
import com.example.hospitals.models.HospitalViewItemModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

sealed class HospitalViewState {

    data class ListOfHospitalsFetched(
        val listOfHospitals: List<HospitalViewItemModel>
    ) : HospitalViewState()

    sealed class Error : HospitalViewState() {
        object ListFetchFailed : Error()
    }
}

class HospitalListViewModel(
    private val hospitalDataRepository: HospitalDataRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<HospitalViewState>()
    val viewState: LiveData<HospitalViewState> = _viewState

    private val compositeDisposable = CompositeDisposable()

    init {
        getListOfHospitals()
    }

    private fun getListOfHospitals(
        filterOption: HospitalFilterOptions = HospitalFilterOptions.DEFAULT
    ) {
        compositeDisposable += hospitalDataRepository.getListOfHospitals(filterOption)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    _viewState.value = HospitalViewState.Error.ListFetchFailed
                },
                onSuccess = {
                    _viewState.value = HospitalViewState.ListOfHospitalsFetched(
                        listOfHospitals = it.mapToViewItemModel()
                    )
                }
            )
    }

    fun onFilterClicked(index: Int) =
        getListOfHospitals(HospitalFilterOptions.getFromIndex(index))

    private fun List<HospitalsDataModel>.mapToViewItemModel(): List<HospitalViewItemModel> =
        map {
            HospitalViewItemModel(
                id = it.id,
                name = it.name,
                city = it.city
            )
        }

    override fun onCleared() {
        // Clean up/cancel any running streams.
        compositeDisposable.clear()
        super.onCleared()
    }
}
