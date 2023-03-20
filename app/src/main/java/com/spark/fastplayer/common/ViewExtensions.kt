package com.spark.fastplayer.common

import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import com.spark.fastplayer.presentation.epg.StreamType
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

fun Program.getFormattedScheduledTime() = scheduleStart?.toBroadCastTime() + " - " + scheduleEnd?.toBroadCastTime()

