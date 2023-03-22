package com.spark.fastplayer.presentation.epg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.presentation.epg.ui.BottomSheetDataState
import com.spark.fastplayer.presentation.epg.ui.HomeScreen
import com.spark.fastplayer.presentation.player.PlaybackState
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val epgViewModel: EPGViewModel by viewModels()

    private var epgState = mutableStateOf<EPGState>(EPGState.Fetch)

    private var bottomSheetDataState = mutableStateOf<BottomSheetDataState>(BottomSheetDataState.Init())

    private var playbackState = mutableStateOf<PlaybackState>(PlaybackState.None)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }

        setContent {
            HomeScreen(
                bottomSheetDataState = bottomSheetDataState,
                epgState = epgState,
                playbackState = playbackState,
                onProgramClick = { channelId, taxonomyId ->
                    epgViewModel.initPlayback(channelId, taxonomyId)
                }
            )
        }
        renderEPGData()
    }

    private fun renderEPGData() {
        lifecycleScope.launchWhenStarted {
            epgViewModel.playbackState.collect{ action->
                when(action) {
                    is PlaybackState.PlaybackSuccess -> {
                        playbackState.value = PlaybackState.PlaybackSuccess(action.metData)
                    }
                    else -> {
                        // not handling error/failure currently
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            epgViewModel.epgState.collect {
                when (it) {
                    is EPGState.FetchSuccess -> {
                        epgState.value = EPGState.FetchSuccess(it.map, it.taxonomies)
                    }
                    is EPGState.FetchError -> {
                        // display error UI
                    }
                    else -> {
                        // not handling error/failure currently
                     }
                }
            }
        }
    }
}
