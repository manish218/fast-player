package com.spark.fastplayer.presentation.epg

sealed class StreamType {

    object None: StreamType()

    object Live: StreamType()

    object Upcoming: StreamType()
}