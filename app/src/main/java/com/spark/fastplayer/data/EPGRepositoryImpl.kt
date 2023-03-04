
package com.spark.fastplayer.data

import com.spark.fastplayer.domain.EPGRepository
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.apis.PlaybackinfoApi
import org.openapitools.client.models.ChannelPlaybackInfo
import org.openapitools.client.models.EpgRow

class EPGRepositoryImpl : EPGRepository {

    override suspend fun getEPGData(epgApi: EpgApi): List<EpgRow> {
        return epgApi.getEpg()
    }

    override suspend fun initPlayBack(channelId: String, playbackInfoApi: PlaybackinfoApi): ChannelPlaybackInfo {
       return playbackInfoApi.getPlaybackinfo(channelId)
    }
}