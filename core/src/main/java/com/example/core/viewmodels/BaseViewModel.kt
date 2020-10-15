package com.example.core.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.lang.RuntimeException

open class BaseViewModel<ViewState> : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState

    protected fun setInitialState(viewState: ViewState) {
        _viewState.value = viewState
    }

    protected fun emitViewState(viewState: (ViewState) -> ViewState) {
        try {
            _viewState.value = viewState(_viewState.value!!)
        } catch (e: Exception) {
            throw RuntimeException("setInitialState must be called first!")
        }
    }
}
