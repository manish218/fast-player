package com.spark.fastplayer.common

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.random.Random

fun Color.Companion.random() : Color {
    val red = Random.nextInt(256)
    val green = Random.nextInt(256)
    val blue = Random.nextInt(256)
    return Color(red, green, blue)
}

@RequiresApi(Build.VERSION_CODES.O)
fun OffsetDateTime.toBroadCastTime(): String? {
    return this.toLocalDateTime()?.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}