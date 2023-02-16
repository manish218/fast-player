package com.spark.fastplayer

import com.spark.fastplayer.common.CoroutineContextProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class TestCoroutineContextProvider(
    val dispatcher: UnconfinedTestDispatcher = UnconfinedTestDispatcher(),
    val scope: TestScope = TestScope(dispatcher)
) : CoroutineContextProvider, TestScope by scope {
    override val main: CoroutineContext get() = dispatcher
    override val io: CoroutineContext get() = dispatcher
    override val unconfined: CoroutineContext get() = dispatcher
    override val default: CoroutineContext get() = dispatcher

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) = this.dispatcher.runBlockingTest(block)
}