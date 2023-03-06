package com.spark.fastplayer.presentation.player

sealed class PlaybackState {

    object None : PlaybackState()

    object Init : PlaybackState()

    class PlaybackSuccess(val metData: PlayBackMetaData) : PlaybackState()

    class PlaybackFailed(message: Throwable) : PlaybackState()

}