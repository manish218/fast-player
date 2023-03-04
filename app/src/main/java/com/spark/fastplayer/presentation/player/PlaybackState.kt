package com.spark.fastplayer.presentation.player

import org.openapitools.client.models.ChannelPlaybackInfo
import org.openapitools.client.models.EpgRow

sealed class PlaybackState {

    object None: PlaybackState()

    object Init: PlaybackState()

    object Loading: PlaybackState()

    class PlaybackSuccess(val streamUrl: String): PlaybackState()

    class PlaybackFailed(message: Throwable): PlaybackState()

}