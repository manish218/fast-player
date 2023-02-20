package com.spark.fastplayer.domain.repository

import org.openapitools.client.apis.EpgApi
import org.openapitools.client.models.EpgRow

interface EPGRepository {
    suspend fun getEPGData(epgApi: EpgApi) : List<EpgRow>
}
