package com.spark.fastplayer.presentation.player

sealed class PlayerState {

    object Init: PlayerState()

    object FullScreenRequest: PlayerState()

    object MinimizeRequest: PlayerState()

}