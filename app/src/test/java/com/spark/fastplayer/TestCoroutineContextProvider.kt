package com.spark.fastplayer

import com.spark.fastplayer.common.CoroutineContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi

class TestCoroutineContextProvider(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : CoroutineContextProvider {
    override val main: CoroutineContext get() = dispatcher
    override val io: CoroutineContext get() = dispatcher
    override val unconfined: CoroutineContext get() = dispatcher
    override val default: CoroutineContext get() = dispatcher
}
