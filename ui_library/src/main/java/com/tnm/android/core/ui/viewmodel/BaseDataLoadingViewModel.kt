package com.tnm.android.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnm.android.core.ui.intent.AppUiIntent
import com.tnm.android.core.ui.state.AppUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

abstract class BaseDataLoadingViewModel<T> : ViewModel() {
    private val _state = MutableStateFlow<AppUiState<T>>(AppUiState.Loading)
    val state: StateFlow<AppUiState<T>> = _state.asStateFlow()
    protected abstract fun dataFlow(param: Any?): Flow<T>

    protected fun setLoading() {
        _state.value = AppUiState.Loading
    }

    protected fun setSuccess(data: T) {
        _state.value = AppUiState.Success(data)
    }

    protected fun setError(message: String) {
        _state.value = AppUiState.Error(message)
    }

    abstract fun handleIntent(intent: AppUiIntent)

    protected open fun fetchData(param: Any? = null) {
        viewModelScope.launch {
            dataFlow(param)
                .onStart {
                    setLoading()
                }
                .catch { e ->
                    setError(e.message.orEmpty())
                }
                .collect { data ->
                    setSuccess(data)
                }
        }
    }

}