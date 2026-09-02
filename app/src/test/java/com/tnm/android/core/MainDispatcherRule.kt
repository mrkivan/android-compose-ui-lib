package com.tnm.android.core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Swaps Dispatchers.Main for a test dispatcher so `viewModelScope` works off-device.
 *
 * Unconfined by default: work launched into viewModelScope runs eagerly, so a test can assert
 * on state right after dispatching an intent. A StandardTestDispatcher would need advancing,
 * and because this rule owns a different scheduler than `runTest`, calling advanceUntilIdle()
 * inside the test would advance the wrong one and the assertions would race.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(val dispatcher: TestDispatcher = UnconfinedTestDispatcher()) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
