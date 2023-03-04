package com.spark.fastplayer.presentation.epg

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.data.repository.EPGData
import com.spark.fastplayer.presentation.epg.ui.Feed
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val epgViewModel: EPGViewModel by viewModels()

    private var epgState = mutableStateOf<EPGState>(EPGState.Fetch)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }

        setContent {
            FastPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Feed(onProgramClick = { }, epgState = epgState.value)
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
                        Toast.makeText(this@EPGActivity, "hello", Toast.LENGTH_LONG).show()
                    }

                    is EPGState.FetchSuccess -> {
                        epgState.value = EPGState.FetchSuccess(it.list, it.taxonomies)
                        // render UI
                        Toast.makeText(this@EPGActivity, "rendering...", Toast.LENGTH_LONG).show()
                    }

                    is EPGState.FetchSuccessSortedData -> {
                        epgState.value = EPGState.FetchSuccessSortedData(it.map, it.taxonomies)
                        // render UI
                        Toast.makeText(this@EPGActivity, "rendering...", Toast.LENGTH_LONG).show()
                    }

                    is EPGState.FetchError -> {
                        // display error UI
                        Toast.makeText(this@EPGActivity, "Error...", Toast.LENGTH_LONG).show()

                    }

                    else -> {

                    }
                }
            }
        }
    }
}

@Composable
fun DefaultPreview(epgData: EPGData) {
    FastPlayerTheme {
       //Feed(onProgramClick = { }, epgData = epgData)
     }
}
