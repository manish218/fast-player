package com.spark.fastplayer.data.repository

import org.openapitools.client.models.EpgRow

data class EPGData (val epgRow: List<EpgRow>, val taxonomies: List<String>)