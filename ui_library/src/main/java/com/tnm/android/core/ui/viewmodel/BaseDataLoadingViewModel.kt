package com.tnm.android.core.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnm.android.core.ui.intent.AppUiIntent
import com.tnm.android.core.ui.state.AppUiState
import kotlinx.coroutines.Job
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

    private var fetchJob: Job? = null
    protected abstract fun dataFlow(param: Any?): Flow<T>

    protected open fun setLoading() {
        _state.value = AppUiState.Loading
    }

    protected open fun setSuccess(data: T) {
        _state.value = AppUiState.Success(data)
    }

    protected open fun setError(message: String) {
        _state.value = AppUiState.Error(message)
    }

    abstract fun handleIntent(intent: AppUiIntent)

    /**
     * Maps a failure from [dataFlow] to the text shown in [AppUiState.Error].
     *
     * Override to localise or to hide technical detail. The default uses the exception message
     * and falls back to a generic string — `e.message.orEmpty()` used to produce `Error("")`
     * and a blank error screen for exceptions without a message.
     */
    protected open fun errorMessage(cause: Throwable): String =
        cause.message?.takeIf { it.isNotBlank() } ?: DEFAULT_ERROR_MESSAGE

    /**
     * Starts (or restarts) collection of [dataFlow].
     *
     * The previous collection is cancelled first: when [dataFlow] is backed by something hot —
     * a Room query, a StateFlow — it never completes, so calling this again would leave the old
     * collector alive and racing the new one for [state]. A few Retry taps would otherwise mean
     * several collectors all writing the UI state.
     */
    protected open fun fetchData(param: Any? = null) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            dataFlow(param)
                .onStart {
                    setLoading()
                }
                .catch { e ->
                    setError(errorMessage(e))
                }
                .collect { data ->
                    setSuccess(data)
                }
        }
    }

    private companion object {
        const val DEFAULT_ERROR_MESSAGE = "Something went wrong. Please try again."
    }
}
