package com.spark.fastplayer.presentation.epg

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.common.activatePlayerLandscapeMode
import com.spark.fastplayer.common.activatePlayerPortraitMode
import com.spark.fastplayer.presentation.player.PlayerAction
import com.spark.fastplayer.presentation.player.PlayerViewModel
import com.spark.fastplayer.presentation.player.VideoPlayerWidget
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }

        setContent {
            FastPlayerTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Black)
                ) {
                    VideoPlayerWidget(
                        videoUri = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        viewModel = playerViewModel
                    )
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            playerViewModel.playerAction.collect{ action->
                when(action) {
                    is PlayerAction.Share -> {
                        Toast.makeText(this@EPGActivity, "Share", Toast.LENGTH_SHORT).show()
                    }
                    is PlayerAction.Liked -> {
                        Toast.makeText(this@EPGActivity, "Liked", Toast.LENGTH_SHORT).show()
                    }
                    is PlayerAction.Info -> {
                        Toast.makeText(this@EPGActivity, "Info", Toast.LENGTH_SHORT).show()
                    }
                    else -> { }
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
    fun DefaultPreview() {

    }
}
