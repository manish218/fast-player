
package com.spark.fastplayer.presentation.epg.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.spark.fastplayer.ui.theme.TextColor
import kotlin.math.ln


@Composable
fun EPGCardItemSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.secondary,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .background(
                color = getBackgroundColorForElevation(color, elevation),
                shape = shape
            )
            .clip(shape)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}


@Composable
private fun getBackgroundColorForElevation(color: Color, elevation: Dp): Color {
    return if (elevation > 0.dp) {
        color.withElevation(elevation)
    } else { color }
}

private fun Color.withElevation(elevation: Dp): Color {
    val foreground = calculateForeground(elevation)
    return foreground.compositeOver(this)
}

private fun calculateForeground(elevation: Dp): Color {
    val alpha = ((4.5f * ln(elevation.value + 1)) + 2f) / 100f
    return TextColor.copy(alpha = alpha)
}
