package com.spark.fastplayer.presentation.epg

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.data.repository.EPGRepositoryImpl
import com.spark.fastplayer.data.repository.TaxonomyPrograms
import com.spark.fastplayer.domain.repository.EPGRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.openapitools.client.apis.EpgApi
import org.openapitools.client.models.EpgRow
import org.openapitools.client.models.Program
import javax.inject.Inject

@HiltViewModel
class EPGViewModel @Inject constructor(
    private val epgRepository: EPGRepository,
    private val coroutineContextProvider: CoroutineContextProvider,
    private val epgApi: EpgApi
): ViewModel() {

    private val _epgState = MutableStateFlow<EPGState>(EPGState.Fetch)
    val epgState = _epgState.asStateFlow()

    fun getEPGData() {
        viewModelScope.launch(coroutineContextProvider.io) {
            val epgData =  epgRepository.getEPGData(epgApi)
            filterTaxonomies(epgData)
        }
    }

    private fun filterTaxonomies(epgList: List<EpgRow>) {
        val  set = HashSet<String> ()
        viewModelScope.launch(coroutineContextProvider.io) {
            val map: Map<String?, List<EpgRow>> = epgList.groupBy { epgRow ->
                epgRow.programs?.firstOrNull()?.taxonomies?.firstOrNull()?.taxonomyId?.orEmpty()
            }
            _epgState.value = EPGState.FetchSuccessSortedData(map.toList(), set.toList())
        }

    }
}
