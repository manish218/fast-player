package com.spark.fastplayer.presentation.epg

import BottomSheetLayout
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetDefaults
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.R
import com.spark.fastplayer.data.repository.EPGData
import com.spark.fastplayer.presentation.epg.ui.EpgProgramsCollection
import com.spark.fastplayer.presentation.epg.ui.EpgTaxonomyCollection
import com.spark.fastplayer.presentation.epg.ui.Feed
import com.spark.fastplayer.presentation.epg.ui.getSelectedTaxonomyIndex
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
               /* Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Feed(onProgramClick = { }, epgState = epgState.value)
                }*/

                BottomSheetLayout()
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(scope: CoroutineScope, state: ModalBottomSheetState) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.black)
            ),
            onClick = {
                scope.launch {
                    state.show()
                }
            }) {
            Text(text = "Open Modal Bottom Sheet Layout")
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
fun bottomSheetVisibility(coroutineScope: CoroutineScope, sheetState: ModalBottomSheetState){
    coroutineScope.launch {
        if (sheetState.currentValue == ModalBottomSheetValue.Hidden) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }
}

