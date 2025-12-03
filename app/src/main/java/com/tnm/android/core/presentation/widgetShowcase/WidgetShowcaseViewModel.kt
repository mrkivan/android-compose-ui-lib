package com.tnm.android.core.presentation.widgetShowcase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnm.android.core.domain.AddTodoTaskUseCase
import com.tnm.android.core.domain.UpdateTodoTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetShowcaseViewModel @Inject constructor(
    private val addTodoTaskUseCase: AddTodoTaskUseCase,
    private val updateTodoTaskUseCase: UpdateTodoTaskUseCase,
) : ViewModel() {

    private val _notificationMessage = MutableSharedFlow<String?>()
    val notificationMessage = _notificationMessage.asSharedFlow()

    private val _navigateToHome = MutableSharedFlow<Boolean>()
    val navigateToHome = _navigateToHome.asSharedFlow()

    private val _showWarningDialog = MutableSharedFlow<Boolean>()
    val showWarningDialog = _showWarningDialog.asSharedFlow()
    private val _navigationEvents = MutableSharedFlow<WidgetShowcaseNavEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun handleIntent(intent: WidgetShowcaseIntent) {
        when (intent) {
            is WidgetShowcaseIntent.LoadData -> {
                // TODO
            }

            is WidgetShowcaseIntent.ShowWarningPopup -> showWarningPopup()
            is WidgetShowcaseIntent.ValidateData -> validateAndSaveData()
            is WidgetShowcaseIntent.NavigateToTaskList -> {
                viewModelScope.launch {
                    _navigationEvents.emit(WidgetShowcaseNavEvent.NavToTaskListScreen)
                }
            }
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