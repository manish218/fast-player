package com.spark.fastplayer.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.presentation.splash.SplashState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel: ViewModel() {


    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Init)
    val playerState = _playerState.asStateFlow()


    fun onFullScreenRequested() {
        viewModelScope.launch {
            _playerState.value = PlayerState.FullScreenRequest
        }
    }

    fun onMinimizeScreenRequested() {
        viewModelScope.launch {
            _playerState.value = PlayerState.MinimizeRequest
        }
    }
}