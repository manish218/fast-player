package com.spark.fastplayer.presentation.epg

import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Program
import org.openapitools.client.models.Taxonomy


sealed class EPGState {
    object Fetch: EPGState()
    class ShowProgramPopUp(val program: Program): EPGState()
    class FetchSuccess(val map: List<Pair<Taxonomy?, List<EpgRow>>>, val taxonomies: List<Taxonomy?>): EPGState()
    object FetchError: EPGState()
}
