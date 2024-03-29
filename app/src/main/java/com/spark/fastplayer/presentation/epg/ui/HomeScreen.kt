package com.spark.fastplayer.presentation.epg.ui

import android.content.res.Configuration
import android.graphics.Rect
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowInsetsControllerCompat
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.spark.fastplayer.presentation.epg.EPGState
import com.spark.fastplayer.presentation.epg.ui.grid.FeedEPGData
import com.spark.fastplayer.presentation.player.PlaybackState
import com.spark.fastplayer.presentation.player.VideoPlayerWidget
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomSheetDataState: MutableState<BottomSheetDataState>,
    epgState: MutableState<EPGState>,
    playbackState: MutableState<PlaybackState>,
    onProgramClick: (String, String) -> Unit,
    onRefreshEPG: () -> Unit,
    refreshInterval: Long = 1000 * 60 * 5L,
    isVideoPlayingInPiPMode: MutableState<Boolean>
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    FastPlayerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ShowLoading(epgState.value)
            ModalBottomSheetLayout(
                sheetState = modalSheetState,
                sheetContent = { BottomSheetLayout(bottomSheetDataState.value) },
                sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                scrimColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            ) {
                Column {
                    RenderPlayer(playbackState = playbackState, isVideoPlayingInPiPMode = isVideoPlayingInPiPMode)
                    EPGGridView(
                        coroutine = coroutineScope,
                        bottomSheetState = modalSheetState,
                        epgState = epgState,
                        bottomSheetDataState = bottomSheetDataState,
                        onProgramClick = onProgramClick
                    )
                }
            }
        }
    }
    forceRefreshEPGView(onRefreshEPG, refreshInterval)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EPGGridView(
    coroutine: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    epgState: MutableState<EPGState>,
    bottomSheetDataState: MutableState<BottomSheetDataState>,
    onProgramClick: (String, String) -> Unit
) {
    FeedEPGData(
        onProgramClick = onProgramClick,
        epgState = epgState.value,
        onProgramLongClick = {
            coroutine.launch {
                bottomSheetDataState.value = BottomSheetDataState.Load(it)
                bottomSheetState.show()
            }
        }
    )
}


@Composable
fun ShowLoading(ePGState: EPGState) {
    if (ePGState == EPGState.Fetch) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            ) {
                CircularProgressIndicator(
                    color = Color.Red,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}

@Composable
fun RenderPlayer(
    playbackState: MutableState<PlaybackState>,
    isVideoPlayingInPiPMode: MutableState<Boolean>
) {
    val configuration = LocalConfiguration.current
    val systemUiController: SystemUiController = rememberSystemUiController()
    val modifier = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            systemUiController.isSystemBarsVisible = false
            systemUiController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .wrapContentSize()
        }
        else -> {
            systemUiController.isSystemBarsVisible = true
            Modifier
                .background(color = MaterialTheme.colorScheme.primary)
                .run {
                    if (!isVideoPlayingInPiPMode.value) fillMaxHeight(0.28f).fillMaxWidth()
                    else this.wrapContentSize()
                }
        }
    }

    Surface(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        VideoPlayerWidget(playbackState = playbackState.value)
    }
}

@Composable
private fun forceRefreshEPGView(onRefreshEPG: () -> Unit, refreshInterval: Long) {
    LaunchedEffect(Unit) {
        while (true) {
            onRefreshEPG.invoke()
            delay(refreshInterval)
        }
    }
}



