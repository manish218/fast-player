package com.spark.fastplayer.presentation.epg

import SheetContent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spark.fastplayer.common.*
import com.spark.fastplayer.presentation.epg.ui.grid.FeedEPGData
import com.spark.fastplayer.presentation.player.PlaybackState
import com.spark.fastplayer.presentation.player.VideoPlayerWidget
import com.spark.fastplayer.presentation.splash.SplashState
import com.spark.fastplayer.presentation.splash.SplashViewModel
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.openapitools.client.models.Program


@AndroidEntryPoint
class EPGActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    private val epgViewModel: EPGViewModel by viewModels()

    private var epgState = mutableStateOf<EPGState>(EPGState.Fetch)
    private var showBtmSht = mutableStateOf<Program?>(null)

    private var playbackState = mutableStateOf<PlaybackState>(PlaybackState.None)

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.splashState.value == SplashState.Init
            }
        }

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val modalSheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden
            )
            ModalBottomSheetLayout(
                sheetState = modalSheetState,
                sheetContent = {
                    Column {
                        SheetContent(showBtmSht.value)
                    }
                },
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                scrimColor = Color.Transparent,
                sheetBackgroundColor =  Color.Transparent
            ){
                MainScreen(coroutine = coroutineScope, bottomSheetState = modalSheetState )
            }

            val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberBottomSheetState(
                    initialValue = BottomSheetValue.Collapsed
                )
            )

           /* BottomSheetScaffold(
                sheetContent = {
                    Column {
                        SheetContent(showBtmSht.value)
                    }
                },
                Modifier.pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    })
                },
                scaffoldState = bottomSheetScaffoldState) {
                MainScreen(coroutine = coroutineScope, bottomSheetState = bottomSheetScaffoldState.bottomSheetState )
            }*/
        }
        renderEPGData()
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainScreen(coroutine: CoroutineScope, bottomSheetState: ModalBottomSheetState ) {
        FastPlayerTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                FeedEPGData(
                    onProgramClick = { },
                    epgState = epgState.value,
                    onProgramLongClick = {
                        coroutine.launch {
                            showBtmSht.value = it
                            bottomSheetState.show()
                           // bottomSheetState.expand()
                        }
                        //epgState.value = EPGState.ShowProgramPopUp(it)
                    })
            }
        }
    }

    private fun renderEPGData() {
        lifecycleScope.launchWhenStarted {
            epgViewModel.playbackState.collect { action ->
                when (action) {
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


    @Composable
    fun showLoading(ePGState: EPGState) {

        if (ePGState == EPGState.Fetch) {
            Dialog(
                onDismissRequest = { },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
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

    @Composable
    fun ShowProgramBottomSheet(program: MutableState<Program?>) {
        if (program != null) {
         //   SheetContent(null,program)
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun RenderPlayer() {
        Column(
            Modifier
                .background(color = Color.Black)
                .fillMaxSize()
        ) {
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
