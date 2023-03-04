package com.spark.fastplayer.presentation.epg.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.openapitools.client.models.Program
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EpgRowsCollection(
    programList: List<Program>,
    onSnackClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            itemsIndexed(programList) { index, program ->

               /* stickyHeader {
                    ChannelImage(
                        imageUrl = program.channel?.images?.firstOrNull()?.url.orEmpty(),
                        contentDescription = "",
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp)
                    )
                }*/

                if (index == 0) {
                    ChannelImage(
                        imageUrl = program.channel?.images?.firstOrNull()?.url.orEmpty(),
                        contentDescription = "",
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp)
                    )
                }
                ChannelItem(program, onSnackClick)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChannelItem(
    program: Program?,
    onSnackClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    JetChannelSurface(
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = { onSnackClick(program?.channel?.url.orEmpty()) })
        ) {
            ChannelName(
                channelName = program?.channel?.title.orEmpty(),
                broadCastTime = program?.scheduleStart?.toBroadCastTime() + " - " +program?.scheduleEnd?.toBroadCastTime(),
                contentDescription = "",
                modifier = Modifier
                    .width(140.dp)
                    .height(60.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun OffsetDateTime.toBroadCastTime() : String?{
   return this.toLocalDateTime()?.format( DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
}
