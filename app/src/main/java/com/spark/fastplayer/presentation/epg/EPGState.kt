package com.spark.fastplayer.presentation.epg

import org.openapitools.client.models.EpgRow


sealed class EPGState {

    object Fetch: EPGState()

    object Loading: EPGState()

    class FetchSuccess(val list: List<EpgRow>): EPGState()

    object FetchError: EPGState()

}