package com.spark.fastplayer.presentation.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.data.pefs.DataStoreManager
import com.spark.fastplayer.domain.repoisitory.EPGRepository
import com.spark.fastplayer.presentation.player.PlayBackMetaData
import com.spark.fastplayer.presentation.player.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.models.Channel
import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Program
import org.openapitools.client.models.Taxonomy
import javax.inject.Inject

@HiltViewModel
class EPGViewModel @Inject constructor(
    private val epgRepository: EPGRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _epgState = MutableStateFlow<EPGState>(EPGState.Fetch)
    val epgState = _epgState.asStateFlow()

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.Init)
    val playbackState = _playbackState.asStateFlow()

    init {
        getEPGData()
    }

    private fun getEPGData() {
        viewModelScope.launch(coroutineContextProvider.io) {
            val epgData = epgRepository.getEPGData()
            if (epgData.isNotEmpty()) resumePlaybackFromHistory(epgData)
        }
    }

    fun initPlayback(channelId: String, taxonomyId: String = "") {
        viewModelScope.launch(coroutineContextProvider.io) {
            val playbackData = epgRepository.getChannelStreamInfo(channelId)
            _playbackState.value = PlaybackState.PlaybackSuccess(
                PlayBackMetaData(
                    streamUrl = playbackData.streamInfo?.streamUrl.orEmpty(),
                    channelLogoUrl = playbackData.channel?.images?.firstOrNull()?.url.orEmpty(),
                    title = playbackData.channel?.title.orEmpty()
                )
            )
            dataStoreManager.saveChannelId(channelId)
            if (taxonomyId.isNotEmpty())dataStoreManager.saveTaxonomyId(taxonomyId)
        }
    }

    private fun filterTaxonomies(epgList: List<EpgRow>) {
        val  set = HashSet<Taxonomy?> ()
        viewModelScope.launch(coroutineContextProvider.io) {
            val map: Map<Taxonomy?, List<EpgRow>> = epgList.groupBy { epgRow ->
               val tx =  epgRow.programs?.firstOrNull()?.taxonomies?.firstOrNull()
                set.add(tx)
                tx
            }
            _epgState.value = EPGState.FetchSuccess(map.toList(), set.toList())
        }
    }

    private fun resumePlaybackFromHistory(epgList: List<EpgRow>) {
        viewModelScope.launch(coroutineContextProvider.io) {
            resumeLastWatchedChannel(
                dataStoreManager.getTaxonomyId.first(),
                dataStoreManager.getChannelId.first(),
                epgList
            )?.let {
                it.channel?.channelid?.let { channelId ->
                    initPlayback(channelId)
                    filterTaxonomies(epgList)
                }
            }
        }
    }

    private fun resumeLastWatchedChannel(taxId : String, channelId: String, epgList: List<EpgRow>): Program? {
        if (channelId.isEmpty() || taxId.isEmpty()) return epgList.firstOrNull()?.programs?.firstOrNull()
       return epgList.flatMap {
            it.programs.asNotNullList()
        }.firstOrNull {
             it.channel?.channelid == channelId
        } ?: getFirstProgramFromLastWatchedTaxonomy(taxId, epgList) ?: epgList.firstOrNull()?.programs?.firstOrNull()
    }

    private fun getFirstProgramFromLastWatchedTaxonomy(taxId: String, epgList: List<EpgRow>): Program? {
        return epgList.flatMap { it.programs.asNotNullList() }.firstOrNull {
            it.taxonomies?.firstOrNull { taxonomy->
                taxonomy.taxonomyId == taxId
            }
            return it
        }
    }

    private fun List<Program>?.asNotNullList(): List<Program> {
        return this ?: emptyList()
    }
}
