package com.spark.fastplayer.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.spark.fastplayer.presentation.epg.ContentType
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.random.Random

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
