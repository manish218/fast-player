package com.spark.fastplayer.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val main: CoroutineContext get() = Dispatchers.Main
    val default: CoroutineContext get() = Dispatchers.Default
    val io: CoroutineContext get() = Dispatchers.IO
    val unconfined: CoroutineContext get() = Dispatchers.Unconfined
}