package com.spark.fastplayer.data.repository

import com.spark.fastplayer.domain.repository.EPGRepository
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.models.EpgRow

class EPGRepositoryImpl : EPGRepository {

    override suspend fun getEPGData(epgApi: EpgApi): List<EpgRow> {
        return epgApi.getEpg()
    }
}