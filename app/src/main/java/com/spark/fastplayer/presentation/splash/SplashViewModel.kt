package com.spark.fastplayer.presentation.splash


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.common.CoroutineContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(coroutineContextProvider: CoroutineContextProvider)  : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Init)
    val splashState = _splashState.asStateFlow()

    init {
        viewModelScope.launch(coroutineContextProvider.io) {
            delay(SPLASH_DELAY_TIMER)
            _splashState.value = SplashState.Loaded
        }
    }

    companion object {
        private const val SPLASH_DELAY_TIMER = 3000L
    }
}
