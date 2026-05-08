package com.tfg_rm.androidapp_restaurantmanager.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * JUnit [TestWatcher] rule that replaces [Dispatchers.Main] with a [TestDispatcher]
 * for the duration of each test. This allows ViewModels that use [viewModelScope]
 * (backed by [Dispatchers.Main]) to run their coroutines eagerly and synchronously
 * inside [kotlinx.coroutines.test.runTest] blocks.
 *
 * Default dispatcher is [UnconfinedTestDispatcher], which executes coroutines
 * immediately without queuing, simplifying assertions on state flows.
 *
 * Usage:
 * ```kotlin
 * @get:Rule val coroutineRule = MainCoroutineRule()
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
