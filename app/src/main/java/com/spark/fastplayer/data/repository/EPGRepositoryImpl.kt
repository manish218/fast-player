
package com.spark.fastplayer.data.repository

import com.spark.fastplayer.domain.repoisitory.EPGRepository
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.apis.PlaybackInfoApi
import org.openapitools.client.models.ChannelPlaybackInfo
import org.openapitools.client.models.EpgRow
import javax.inject.Inject

class EPGRepositoryImpl @Inject constructor(
    private val epgApi: EpgApi,
    private val playbackInfoApi: PlaybackInfoApi
) : EPGRepository {

    override suspend fun getEPGData(): List<EpgRow> {
        return epgApi.getEpg()
    }

    override suspend fun getChannelStreamInfo(channelId: String): ChannelPlaybackInfo {
       return playbackInfoApi.getPlaybackinfo(channelId)
    }
}