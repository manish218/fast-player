package com.spark.fastplayer.presentation.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel: ViewModel() {


    private val _playerAction = MutableStateFlow<PlayerAction>(PlayerAction.None)
    val playerAction = _playerAction.asStateFlow()


    fun onShareClicked() {
        viewModelScope.launch {
            _playerAction.value = PlayerAction.Share
        }
    }

    fun onInfoClicked() {
        viewModelScope.launch {
            _playerAction.value = PlayerAction.Info
        }
    }

    fun onLikeClicked() {
        viewModelScope.launch {
            _playerAction.value = PlayerAction.Liked
        }
    }
}