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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val epgViewModel: EPGViewModel by viewModels()

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
                    Greeting("Fast Player")
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
                        // render UI
                        Toast.makeText(this@EPGActivity, "rendering...", Toast.LENGTH_LONG).show()
                    }

                    is EPGState.FetchError -> {
                        // display error UI
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FastPlayerTheme {
        Greeting("Android")
    }
}
