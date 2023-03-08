
package com.spark.fastplayer.presentation.epg.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spark.fastplayer.R
import com.spark.fastplayer.common.isLive
import com.spark.fastplayer.common.toBroadCastTime
import com.spark.fastplayer.presentation.epg.ContentType
import org.openapitools.client.models.Program
import kotlin.math.ln


@Preview
@Composable
fun DefaultView() {
    ChannelName(
        channelName = "The Monster animal",
        broadCastTime = "8:30 - 9:30 PM",
        modifier = Modifier
            .height(50.dp)
            .width(120.dp)
    )
}

@Composable
fun ChannelName(
    channelName: String?,
    broadCastTime: String?,
    contentType: ContentType = ContentType.None,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    JetChannelSurface(
        color = Color.DarkGray,
        elevation = elevation,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        border = BorderStroke(1.dp, Color.Gray)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(modifier = Modifier.fillMaxWidth().align(Alignment.Start).padding(start = 10.dp, bottom = 10.dp)) {
                Text(
                    text = channelName.orEmpty(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
            ) {
                Text(
                    text = broadCastTime.orEmpty(),
                    fontSize = 12.sp,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Box(modifier  = Modifier
                    .background(if (contentType is ContentType.Live) Color.Red else Color.Gray)
                    .padding(1.dp)) {
                        Text(
                            text = if (contentType is ContentType.Live) "Live" else "UpComing",
                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
            }
        }
    }
}


@Composable
fun ChannelImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    JetChannelSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier,
        border = BorderStroke(2.dp, Color.White)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(R.drawable.ic_baseline_connected_tv_24),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
fun JetChannelSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = Color.Black,
    contentColor: Color = Color.Gray,
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgramCardView(
    program: Program?,
    onProgramClicked: (String) -> Unit,
) {
    JetChannelSurface(
        modifier = Modifier
            .width(160.dp)
            .height(72.dp)
            .padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = { onProgramClicked(program?.channel?.url.orEmpty()) })
        ) {
            ChannelName(
                channelName = program?.channel?.title.orEmpty(),
                contentType = program?.scheduleStart?.isLive(program.scheduleEnd!!)!!,
                broadCastTime = program.scheduleStart?.toBroadCastTime() + " - " + program?.scheduleEnd?.toBroadCastTime(),
                modifier = Modifier
                    .width(160.dp)
                    .height(72.dp)
            )
        }
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
    return Color.White.copy(alpha = alpha)
}
