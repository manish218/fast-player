package com.spark.fastplayer.domain.repoisitory

import org.openapitools.client.models.ChannelPlaybackInfo
import org.openapitools.client.models.EpgRow

interface EPGRepository {
    suspend fun getEPGData() : List<EpgRow>

    suspend fun getChannelStreamInfo(channelId: String) : ChannelPlaybackInfo
}