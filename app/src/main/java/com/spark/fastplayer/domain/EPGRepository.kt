package com.spark.fastplayer.domain

import org.openapitools.client.apis.EpgApi
import org.openapitools.client.apis.PlaybackinfoApi
import org.openapitools.client.models.ChannelPlaybackInfo
import org.openapitools.client.models.EpgRow

interface EPGRepository {
    suspend fun getEPGData(epgApi: EpgApi) : List<EpgRow>

    suspend fun initPlayBack(channelId: String, playbackInfoApi: PlaybackinfoApi) : ChannelPlaybackInfo
}