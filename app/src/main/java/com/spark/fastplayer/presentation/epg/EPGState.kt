package com.spark.fastplayer.presentation.epg

import com.spark.fastplayer.data.repository.TaxonomyPrograms
import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Taxonomy


sealed class EPGState {
    object Fetch: EPGState()
    object Loading: EPGState()
    class FetchSuccess(val list: List<EpgRow>, val taxonomies: List<String>): EPGState()
    class FetchSuccessSortedData(val map: List<Pair<Taxonomy?, List<EpgRow>>>, val taxonomies: List<Taxonomy?>): EPGState()
    class FetchSuccessGroupedData(val list: List<TaxonomyPrograms>, val taxonomies: List<String>): EPGState()
    object FetchError: EPGState()
}
