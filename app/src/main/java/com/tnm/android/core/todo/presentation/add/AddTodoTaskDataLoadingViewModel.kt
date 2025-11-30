package com.tnm.android.core.todo.presentation.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnm.android.core.todo.domain.AddTodoTaskUseCase
import com.tnm.android.core.todo.domain.UpdateTodoTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTodoTaskDataLoadingViewModel @Inject constructor(
    private val addTodoTaskUseCase: AddTodoTaskUseCase,
    private val updateTodoTaskUseCase: UpdateTodoTaskUseCase,
) : ViewModel() {

    private val _notificationMessage = MutableSharedFlow<String?>()
    val notificationMessage = _notificationMessage.asSharedFlow()

    private val _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    private val _showWarningDialog = MutableSharedFlow<Boolean>()
    val showWarningDialog = _showWarningDialog.asSharedFlow()


    fun handleIntent(intent: AddTodoTaskIntent) {
        when (intent) {
            is AddTodoTaskIntent.LoadData -> {
                // TODO
            }

            is AddTodoTaskIntent.ShowWarningPopup -> showWarningPopup()
            is AddTodoTaskIntent.ValidateData -> validateAndSaveData()
        }

    }

    fun validateDate(): Boolean {
        // TODO
        return true
    }

    fun showWarningPopup() {
        // TODO
        viewModelScope.launch {
            _navigateToHome.emit(true)
        }
    }

    private fun setNotificationMessage(message: String?) {
        viewModelScope.launch {
            _notificationMessage.emit(message)
        }
    }

    private fun validateAndSaveData() {
        // TODO
        viewModelScope.launch {
            _navigateToHome.emit(true)
        }
    }


}