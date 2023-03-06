package com.spark.fastplayer.presentation.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spark.fastplayer.common.CoroutineContextProvider
import com.spark.fastplayer.domain.repoisitory.EPGRepository
import com.spark.fastplayer.presentation.player.PlayBackMetaData
import com.spark.fastplayer.presentation.player.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EPGViewModel @Inject constructor(
    private val epgRepository: EPGRepository,
    private val coroutineContextProvider: CoroutineContextProvider
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
            _epgState.value = EPGState.FetchSuccess(epgData)
        }
    }

    fun initPlayback(channelId: String) {
        viewModelScope.launch(coroutineContextProvider.io) {
            val playbackData = epgRepository.getChannelStreamInfo(channelId)
            _playbackState.value = PlaybackState.PlaybackSuccess(
                PlayBackMetaData(
                    streamUrl = playbackData.streamInfo?.streamUrl.orEmpty(),
                    channelLogoUrl = playbackData.channel?.images?.firstOrNull()?.url.orEmpty(),
                    title = playbackData.channel?.title.orEmpty()
                )
            )
        }
    }
}
