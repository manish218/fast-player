package com.spark.fastplayer.presentation.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.data.repository.EPGRepositoryImpl
import com.spark.fastplayer.domain.repository.EPGRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.openapitools.client.apis.EpgApi
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
            _epgState.value = EPGState.FetchSuccess(epgData)
        }
    }
}
