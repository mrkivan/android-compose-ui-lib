package com.tnm.android.core.presentation.widgetShowcase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tnm.android.core.ui.intent.AppUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetShowcaseViewModel @Inject constructor() : ViewModel() {

    // Channel, not SharedFlow: navigation and messages are one-shot effects that must not replay
    // on rotation. The previous four separate SharedFlows overlapped, and one of them was never
    // emitted to at all.
    private val _events = Channel<WidgetShowcaseEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun handleIntent(intent: AppUiIntent) {
        when (intent) {
            is WidgetShowcaseIntent.BackPressed ->
                emit(WidgetShowcaseEvent.ShowLeaveWarning)

            is WidgetShowcaseIntent.ConfirmLeave ->
                emit(WidgetShowcaseEvent.NavigateBack)

            is WidgetShowcaseIntent.ValidateData ->
                emit(WidgetShowcaseEvent.NavigateBack)

            is WidgetShowcaseIntent.NavigateToTaskList ->
                emit(WidgetShowcaseEvent.NavigateToTaskList)
        }
    }

    private fun emit(event: WidgetShowcaseEvent) {
        viewModelScope.launch { _events.send(event) }
    }
}
