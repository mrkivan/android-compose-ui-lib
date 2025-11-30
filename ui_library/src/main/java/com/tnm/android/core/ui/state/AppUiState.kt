package com.tnm.android.core.ui.state

sealed interface AppUiState<out T> {
    object Loading : AppUiState<Nothing>
    data class Success<T>(val data: T) : AppUiState<T>
    data class Error(val message: String) : AppUiState<Nothing>
}