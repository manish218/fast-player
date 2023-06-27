package com.spark.fastplayer.presentation.epg

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Bundle
import android.util.Rational
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.common.hasPipSupport
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

    private var pipModeState = mutableStateOf<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }
        actionBar?.hide()
        setContent {
            HomeScreen(
                bottomSheetDataState = bottomSheetDataState,
                epgState = epgState,
                playbackState = playbackState,
                onProgramClick = { channelId, taxonomyId ->
                    epgViewModel.initPlayback(channelId, taxonomyId)
                },
                isVideoPlayingInPiPMode = pipModeState,
                onRefreshEPG = { epgViewModel.sanitizeEPGData() }
            )
        }
    }

    private fun renderEPGData() {
        lifecycleScope.launchWhenStarted {
            epgViewModel.playbackState.collect{ action->
                when(action) {
                    is PlaybackState.PlaybackSuccess -> {
                        playbackState.value = PlaybackState.PlaybackSuccess(action.metData)
                    }
                    is PlaybackState.Init -> {
                        playbackState.value = PlaybackState.Init
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


    private fun updatedPipParams(): PictureInPictureParams {
        return PictureInPictureParams.Builder()
            .setAspectRatio(Rational(16, 9))
            .build()

    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        pipModeState.value = isInPictureInPictureMode
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
    }

    override fun onStart() {
        super.onStart()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        renderEPGData()
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if(hasPipSupport()) {
            enterPictureInPictureMode(updatedPipParams())
        }
    }

    override fun onStop() {
        super.onStop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
