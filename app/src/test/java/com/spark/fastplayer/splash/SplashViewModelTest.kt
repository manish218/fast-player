package com.spark.fastplayer.splash

import app.cash.turbine.test
import com.spark.fastplayer.MainCoroutineRule
import com.spark.fastplayer.TestCoroutineContextProvider
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val rule = MainCoroutineRule(StandardTestDispatcher())

    private lateinit var viewModel: SplashViewModel
    @Before
    fun setUp() {
        viewModel = SplashViewModel(TestCoroutineContextProvider())
    }
    @Test
    fun splashViewModel_state_Test()  {
        runTest {
            val initialState = viewModel.splashState.first()
            Assert.assertTrue(initialState is SplashState.Init)

            advanceUntilIdle()
            viewModel.splashState.test {
                val state = this.awaitItem()
                Assert.assertTrue(state is SplashState.Loaded)
            }
        }
    }
}
