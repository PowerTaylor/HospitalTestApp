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

data class ViewState(
    val listOfHospitals: List<HospitalViewItemModel> = listOf(),
    val currentFilterOptionIndex: Int = 0,
    val showFilter: Boolean = false,
    val showError: Boolean = false
)

class HospitalListViewModel(
    private val hospitalDataRepository: HospitalDataRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    private val compositeDisposable = CompositeDisposable()

    private var currentFilterOptionIndex: Int = 0

    init {
        getListOfHospitals()
    }

    private fun getListOfHospitals(
        filterOption: HospitalFilterOptions = HospitalFilterOptions.DEFAULT
    ) {
        // Progress bar could be added if performance was slow via: .doOnSubscribe {  }
        compositeDisposable += hospitalDataRepository.getListOfHospitals(filterOption)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    _viewState.value = currentViewState().copy(showError = true)
                },
                onSuccess = {
                    _viewState.value = currentViewState()
                        .copy(
                            listOfHospitals = it.mapToViewItemModel(),
                            showError = false,
                            showFilter = false
                        )
                }
            )
    }

    private fun currentViewState(): ViewState {
        return viewState.value ?: ViewState()
    }

    fun onFilterClicked() {
        _viewState.value = currentViewState()
            .copy(
                showFilter = true,
                currentFilterOptionIndex = currentFilterOptionIndex
            )
    }

    fun onFilterConfirmed(index: Int) {
        currentFilterOptionIndex = index
        getListOfHospitals(filterOption = HospitalFilterOptions.getFromIndex(currentFilterOptionIndex))
    }

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
