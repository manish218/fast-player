package com.spark.fastplayer.presentation.epg

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.common.activatePlayerLandscapeMode
import com.spark.fastplayer.common.activatePlayerPortraitMode
import com.spark.fastplayer.presentation.player.PlaybackState
import com.spark.fastplayer.presentation.player.VideoPlayerWidget
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val epgViewModel: EPGViewModel by viewModels()

    private var playbackState = mutableStateOf<PlaybackState>(PlaybackState.None)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }

        setContent {
            FastPlayerTheme {
                RenderPlayer()
            }
        }

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
            epgViewModel.epgState.collect{
                when(it) {
                    is EPGState.FetchSuccess -> {
                        epgViewModel.initPlayback(it.list.firstOrNull()?.programs?.first()?.channel?.channelid.orEmpty())
                     }
                    else -> {
                        // not handling error/failure currently
                     }
                }
            }
        }
    }

    override fun onConfigurationChanged(config: Configuration) {
        super.onConfigurationChanged(config)
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           window.activatePlayerLandscapeMode()
        } else {
            window.activatePlayerPortraitMode()
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun RenderPlayer() {
        Column(
            Modifier
                .background(color = Color.Black)
                .fillMaxSize()) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Black)
            ) {
               VideoPlayerWidget(playbackState = playbackState.value)
            }
        }
    }
}
