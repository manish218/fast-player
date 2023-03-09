package com.spark.fastplayer.presentation.epg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.presentation.epg.ui.grid.FeedEPGData
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val epgViewModel: EPGViewModel by viewModels()

    private var epgState = mutableStateOf<EPGState>(EPGState.Fetch)

    private var showLoading = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }

        setContent {
            FastPlayerTheme {
                showLoading(showLoading.value)
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FeedEPGData(onProgramClick = { }, epgState = epgState.value)
                }
            }
        }

        renderEPGData()
        epgViewModel.getEPGData()
    }

    private fun renderEPGData() {
        lifecycleScope.launchWhenStarted {

            epgViewModel.epgState.collect{

                when(it) {
                    is EPGState.Fetch -> {
                        showLoading.value = true
                    }

                    is EPGState.FetchSuccess -> {
                        epgState.value = EPGState.FetchSuccess(it.map, it.taxonomies)
                        showLoading.value = false
                    }

                    is EPGState.FetchError -> {
                        // display error UI
                    }

                    else -> {

                    }
                }
            }
        }
    }
}

@Composable
fun showLoading(showDialogValue: Boolean) {

    if (showDialogValue) {
        Dialog(
            onDismissRequest = {   },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment= Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
            ) {
                /*TO DO */
                 //current theme doesn't support loading indicator
                //CircularProgressIndicator(LocalContext.current)
            }
        }
    }
}
