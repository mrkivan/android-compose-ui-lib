package com.tnm.android.core.ui.viewmodel

import com.tnm.android.core.ui.intent.AppUiIntent
import com.tnm.android.core.ui.state.AppUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class BaseDataLoadingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private object Load : AppUiIntent

    /** Test double: [source] is what [dataFlow] returns; [collectorsCancelled] counts cancelled collections. */
    private class TestViewModel(private val source: () -> Flow<String>) : BaseDataLoadingViewModel<String>() {
        var collectorsCancelled = 0
        var lastParam: Any? = null

        override fun dataFlow(param: Any?): Flow<String> {
            lastParam = param
            return source().onCompletion { cause -> if (cause != null) collectorsCancelled++ }
        }

        override fun handleIntent(intent: AppUiIntent) {
            if (intent === Load) fetchData()
        }

        fun load(param: Any?) = fetchData(param)
    }

    @Test
    fun `starts in loading before anything is fetched`() {
        val viewModel = TestViewModel { flow { emit("x") } }

        assertTrue(viewModel.state.value is AppUiState.Loading)
    }

    @Test
    fun `emits success with the data`() = runTest {
        val viewModel = TestViewModel { flow { emit("hello") } }

        viewModel.handleIntent(Load)

        assertEquals(AppUiState.Success("hello"), viewModel.state.value)
    }

    @Test
    fun `passes the param through to dataFlow`() = runTest {
        val viewModel = TestViewModel { flow { emit("x") } }

        viewModel.load(42)

        assertEquals(42, viewModel.lastParam)
    }

    @Test
    fun `emits error with the exception message`() = runTest {
        val viewModel = TestViewModel { flow { throw IllegalStateException("boom") } }

        viewModel.handleIntent(Load)

        assertEquals(AppUiState.Error("boom"), viewModel.state.value)
    }

    @Test
    fun `falls back to a non blank message when the exception has none`() = runTest {
        // Regression: e.message.orEmpty() produced Error("") and a blank error screen.
        val viewModel = TestViewModel { flow { throw IllegalStateException() } }

        viewModel.handleIntent(Load)

        val state = viewModel.state.value
        assertTrue(state is AppUiState.Error)
        assertTrue((state as AppUiState.Error).message.isNotBlank())
    }

    @Test
    fun `refetching cancels the previous hot collector`() = runTest {
        // A StateFlow never completes, so without cancellation two collectors would race for state.
        val hot = MutableStateFlow("first")
        val viewModel = TestViewModel { hot }

        viewModel.handleIntent(Load)
        viewModel.handleIntent(Load)

        assertEquals(1, viewModel.collectorsCancelled)
        assertEquals(AppUiState.Success("first"), viewModel.state.value)
    }

    @Test
    fun `hot source keeps updating state after the first emission`() = runTest {
        val hot = MutableStateFlow("first")
        val viewModel = TestViewModel { hot }

        viewModel.handleIntent(Load)
        hot.value = "second"

        assertEquals(AppUiState.Success("second"), viewModel.state.value)
    }
}
