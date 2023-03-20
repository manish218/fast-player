package com.spark.fastplayer.presentation.epg.ui

import BottomSheetLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.spark.fastplayer.presentation.epg.EPGState
import com.spark.fastplayer.presentation.epg.ui.grid.FeedEPGData
import com.spark.fastplayer.presentation.player.PlaybackState
import com.spark.fastplayer.presentation.player.VideoPlayerWidget
import com.spark.fastplayer.ui.theme.FastPlayerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    bottomSheetDataState: MutableState<BottomSheetDataState>,
    epgState: MutableState<EPGState>
) {
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = { BottomSheetLayout(bottomSheetDataState.value) },
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.Black.copy(alpha = 0.4f),
    ) {
        EPGGridView(
            coroutine = coroutineScope,
            bottomSheetState = modalSheetState,
            epgState = epgState,
            bottomSheetDataState = bottomSheetDataState
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EPGGridView(
    coroutine: CoroutineScope,
    bottomSheetState: ModalBottomSheetState,
    epgState: MutableState<EPGState>,
    bottomSheetDataState: MutableState<BottomSheetDataState>
) {
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
                        bottomSheetDataState.value = BottomSheetDataState.Load(it)
                        bottomSheetState.show()
                    }
                })
        }
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
fun RenderPlayer(playbackState: MutableState<PlaybackState>) {
    Column(
        Modifier
            .background(color = Color.Black)
            .fillMaxSize()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.Black)
        ) {
            VideoPlayerWidget(playbackState = playbackState.value)
        }
    }
}