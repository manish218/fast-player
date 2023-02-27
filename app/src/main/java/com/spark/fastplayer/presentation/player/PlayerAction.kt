package com.spark.fastplayer.presentation.player

sealed class PlayerAction {

    object None: PlayerAction()

    object Share: PlayerAction()

    object Liked: PlayerAction()

    object Info: PlayerAction()

}