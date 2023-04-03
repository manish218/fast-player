package com.spark.fastplayer.presentation.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.common.isExpired
import com.spark.fastplayer.data.pefs.DataStoreManager
import com.spark.fastplayer.domain.repoisitory.EPGRepository
import com.spark.fastplayer.presentation.player.PlayBackMetaData
import com.spark.fastplayer.presentation.player.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.None)
    val playbackState = _playbackState.asStateFlow()

    private val epgList = mutableListOf<EpgRow>()
    private val sanitizedEPGList = mutableListOf<EpgRow>()

    init {
        getEPGData()
    }

    private fun getEPGData() {
        viewModelScope.launch(coroutineContextProvider.io) {
            val epgData = epgRepository.getEPGData()
            epgList.addAll(epgData)
            if (epgData.isNotEmpty()) resumePlaybackFromHistory(epgData)
            filterTaxonomies(epgData)
        }
    }

    fun initPlayback(channelId: String, taxonomyId: String) {
        viewModelScope.launch(coroutineContextProvider.io) {
            val playbackData = epgRepository.getChannelStreamInfo(channelId)
            _playbackState.value = PlaybackState.PlaybackSuccess(
                PlayBackMetaData(
                    streamUrl = playbackData.streamInfo?.streamUrl.orEmpty(),
                    channelLogoUrl = playbackData.channel?.images?.firstOrNull()?.url.orEmpty(),
                    title = playbackData.channel?.title.orEmpty(),
                    description = playbackData.channel?.description.orEmpty()
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

    fun sanitizeEPGData() {
        viewModelScope.launch(coroutineContextProvider.io) {
            if (epgList.isNotEmpty()) {
                val forceRefreshEPG = filterExpiredPrograms()
                if (forceRefreshEPG)  {
                    filterTaxonomies(sanitizedEPGList)
                    epgList.apply {
                        clear()
                        addAll(sanitizedEPGList)
                    }
                }
            }
        }
    }

    private fun filterExpiredPrograms(): Boolean {
        var expiredProgramExists = false
        sanitizedEPGList.clear()
        epgList.forEach {
            val validPList: List<Program> =  it.programs.asNotNullList().filter { program ->
                val isExpired =  program.isExpired()
                if (isExpired) expiredProgramExists = true
                filter@ !isExpired
            }
            sanitizedEPGList.add(EpgRow(validPList))
        }
        return expiredProgramExists
    }

    private fun resumePlaybackFromHistory(epgList: List<EpgRow>) {
        viewModelScope.launch(coroutineContextProvider.io) {
            _playbackState.value = PlaybackState.Init
            if (dataStoreManager.getTaxonomyId.first().isEmpty() && dataStoreManager.getChannelId.first().isEmpty()) {
                playFirstEPGProgram(epgList)
            } else {
                getLastWatchedProgram(
                    dataStoreManager.getTaxonomyId.first(),
                    dataStoreManager.getChannelId.first(),
                    epgList
                )?.let { program ->
                    program.channel?.channelid?.let { channelId ->
                        initPlayback(
                            channelId = channelId,
                            taxonomyId = program.taxonomies?.firstOrNull()?.taxonomyId.orEmpty()
                        )
                    }
                }
            }
        }
    }
    private fun playFirstEPGProgram(epgList: List<EpgRow>) {
        epgList.firstOrNull()?.programs?.firstOrNull()?.let { program ->
            program.channel?.channelid?.let {
                initPlayback(
                    channelId = it,
                    taxonomyId = program.taxonomies?.first()?.taxonomyId.orEmpty()
                )
            }
        }
    }
    private fun getLastWatchedProgram(taxonomyId : String, channelId: String, epgList: List<EpgRow>): Program? {
       return getLastWatchedChannel(channelId, epgList)
           ?: getFirstProgramFromLastWatchedTaxonomy(taxonomyId, epgList)
           ?: epgList.firstOrNull()?.programs?.firstOrNull()
    }

    private fun getLastWatchedChannel(channelId: String, epgList: List<EpgRow>): Program? {
        return epgList.flatMap { it.programs.asNotNullList() }.takeIf { it.isNotEmpty() }
            ?.firstOrNull { it.channel?.channelid == channelId }
    }
    private fun getFirstProgramFromLastWatchedTaxonomy(taxId: String, epgList: List<EpgRow>): Program? {
        return epgList.flatMap { it.programs.asNotNullList() }.takeIf { it.isNotEmpty() }?.firstOrNull {
            it.taxonomies?.firstOrNull { taxonomy->
                taxonomy.taxonomyId == taxId
            }
            return it
        }
    }

    private fun List<Program>?.asNotNullList(): List<Program> =  this ?: emptyList()

}
