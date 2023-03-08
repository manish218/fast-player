package com.spark.fastplayer.presentation.epg

sealed class ContentType {

    object None: ContentType()

    object Live: ContentType()

    object Upcoming: ContentType()
}