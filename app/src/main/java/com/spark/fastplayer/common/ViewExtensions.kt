package com.spark.fastplayer.common

import BottomSheetLayout
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.spark.fastplayer.presentation.epg.StreamType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.openapitools.client.models.Program
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import kotlin.random.Random

inline fun Modifier.noRippleClickable(
    crossinline onClick: () -> Unit
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun Window.activatePlayerLandscapeMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.hide(
            android.view.WindowInsets.Type.statusBars()
        )
    } else {
        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val uiOptions =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}

fun Window.activatePlayerPortraitMode() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.show(
            android.view.WindowInsets.Type.statusBars()
        )
    } else {
        val uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = uiOptions
        clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}

fun Color.Companion.random(): Color {
    val red = Random.nextInt(128)
    val green = Random.nextInt(128)
    val blue = Random.nextInt(128)
    return Color(red, green, blue)
}

fun OffsetDateTime.toBroadCastTime(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().toString()
    } else {
        TODO("VERSION.SDK_INT < O")
    }
}
fun OffsetDateTime?.getStreamType(scheduleEndTime: OffsetDateTime?): StreamType {
    return if (this == null || scheduleEndTime == null) StreamType.None
    else {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val currentLocalTime = Instant.now()
             val scheduleStartInLocalTimeZone = toInstant().atZone(ZoneId.systemDefault()).toInstant()
             val scheduleEndInLocalTimeZone = scheduleEndTime.toInstant().atZone(ZoneId.systemDefault()).toInstant()
             if (currentLocalTime.isBefore(scheduleStartInLocalTimeZone)) {
                 StreamType.Upcoming
             } else if (currentLocalTime.isAfter(scheduleStartInLocalTimeZone) &&currentLocalTime.isBefore(scheduleEndInLocalTimeZone)) {
                 StreamType.Live
             } else StreamType.None
         } else {
             TODO("VERSION.SDK_INT < O")
         }
    }
}

fun Activity.showAsBottomSheet(content: @Composable (() -> Unit) -> Unit
) {
    val viewGroup = this.findViewById(android.R.id.content) as ViewGroup
    addContentToView( viewGroup, content)
}

fun addContentToView(
    viewGroup: ViewGroup,
    content: @Composable (() -> Unit) -> Unit
) {
    viewGroup.addView(
        ComposeView(viewGroup.context).apply {
            setContent {
                BottomSheetWrapper( viewGroup, this, content)
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun BottomSheetWrapper(
    parent: ViewGroup,
    composeView: ComposeView,
    content: @Composable (() -> Unit) -> Unit
) {
    val TAG = parent::class.java.simpleName
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState =
        rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                it != ModalBottomSheetValue.HalfExpanded
            }
        )
    var isSheetOpened by remember { mutableStateOf(false) }

    ModalBottomSheetLayout(
        sheetBackgroundColor = Color.Transparent,
        sheetState = modalBottomSheetState,
        sheetContent = {
            content {
                animateHideBottomSheet(coroutineScope, modalBottomSheetState)
            }
        }
    ) {}


    BackHandler {
        animateHideBottomSheet(coroutineScope, modalBottomSheetState)
    }

    // Take action based on hidden state
    LaunchedEffect(modalBottomSheetState.currentValue) {
        when (modalBottomSheetState.currentValue) {
            ModalBottomSheetValue.Hidden -> {
                when {
                    isSheetOpened -> parent.removeView(composeView)
                    else -> {
                        isSheetOpened = true
                        modalBottomSheetState.show()
                    }
                }
            }
            else -> {
                Log.i(TAG, "Bottom sheet ${modalBottomSheetState.currentValue} state")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun animateHideBottomSheet(
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    coroutineScope.launch {
        modalBottomSheetState.hide() // will trigger the LaunchedEffect
    }
}

