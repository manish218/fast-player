package com.spark.fastplayer.presentation.splash

sealed class SplashState {

    object Init: SplashState()

    object Loaded: SplashState()
}