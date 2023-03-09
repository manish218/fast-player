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
import com.spark.fastplayer.presentation.epg.ContentType
import java.time.Instant
import java.time.OffsetDateTime
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

fun Color.Companion.random() : Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}

fun OffsetDateTime.toBroadCastTime(): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.toOffsetTime().toLocalTime().toString()
    } else {
        TODO("VERSION.SDK_INT < O")
    }
}
fun OffsetDateTime.isLive(scheduleEndTime: OffsetDateTime): ContentType {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         if (Instant.now().isBefore(this.toInstant())) {
            ContentType.Upcoming
        } else if (Instant.now().isAfter(this.toInstant()) && Instant.now()
                .isBefore(scheduleEndTime.toInstant())
        ) {
            ContentType.Live
        } else ContentType.None
    } else {
        TODO("VERSION.SDK_INT < O")
    }
}
