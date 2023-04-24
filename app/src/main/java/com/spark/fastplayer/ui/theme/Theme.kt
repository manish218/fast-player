package com.spark.fastplayer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    primary = primary,
    secondary = secondaryColor,
    tertiary = tertiary,
    background = BackgroundColor,
    secondaryContainer = SurfaceColor,
    surface = SurfaceColor,
    onPrimary = TextColor,
    onBackground = BackgroundColor
)

private val LightColorScheme = lightColorScheme(
    primary = Color.Black,
    secondary = secondaryColor,
    tertiary = tertiary,
    background = Color.Black,
    secondaryContainer = Color.DarkGray,
    surface = SurfaceColor,
    onPrimary = TextColor,
    onBackground = BackgroundColor,
)

@Composable
fun FastPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val sysUiController = rememberSystemUiController()
    SideEffect {
        sysUiController.setSystemBarsColor(
            color = Color.Black
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
