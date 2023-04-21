package com.spark.fastplayer.presentation.epg.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spark.fastplayer.common.getFormattedScheduledTime
import com.spark.fastplayer.common.getStreamType
import com.spark.fastplayer.presentation.epg.StreamType
import com.spark.fastplayer.presentation.epg.ui.BottomSheetDataState
import com.spark.fastplayer.ui.theme.LiveBadgeColor
import com.spark.fastplayer.ui.theme.UpcomingBadgeColor
import org.openapitools.client.models.Program

@Composable
fun BottomSheetLayout(bottomSheetDataState: BottomSheetDataState) {
    when (bottomSheetDataState) {
        is BottomSheetDataState.Load -> SheetContent(program = bottomSheetDataState.program)
        is BottomSheetDataState.Init -> { }
    }
}

@Composable
fun SheetContent(program: Program) {
    val streamTye = program.scheduleStart.getStreamType(program.scheduleEnd)
    Column(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(program.channel?.images?.firstOrNull()?.url.orEmpty())
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .width(108.dp)
                    .height(64.dp),
                contentScale = ContentScale.Fit,
            )

            Box(
                modifier = Modifier
                    .background(shape = RoundedCornerShape(4.dp),  color = if (streamTye is StreamType.Live) LiveBadgeColor else UpcomingBadgeColor)
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            ) {
                androidx.compose.material3.Text(
                    text = if (streamTye is StreamType.Live) "Live" else "Upcoming",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                )

            }
        }

        Text(
            text = program.title.orEmpty(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = program.description.orEmpty(),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = program.getFormattedScheduledTime(),
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .background(shape = RoundedCornerShape(4.dp), color = MaterialTheme.colorScheme.primary)
                .padding(8.dp)
        ) {
            androidx.compose.material3.Text(
                text = program.channel?.taxonomies?.firstOrNull()?.title.orEmpty(),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

