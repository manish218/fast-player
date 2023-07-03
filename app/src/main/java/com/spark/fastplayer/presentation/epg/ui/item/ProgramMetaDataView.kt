package com.spark.fastplayer.presentation.epg.ui.item

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spark.fastplayer.common.getFormattedScheduledTime
import com.spark.fastplayer.common.getStreamType
import com.spark.fastplayer.presentation.epg.StreamType
import com.spark.fastplayer.presentation.epg.ui.EPGCardItemSurface
import org.openapitools.client.models.Program

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProgramCardView(
    program: Program,
    onProgramClicked: (String, String) -> Unit,
    onLongPressedCallback: (Program) -> Unit
) {
    EPGCardItemSurface(
        modifier = Modifier
            .padding(
                start = 4.dp,
                end = 4.dp,
                bottom = 8.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(160.dp)
                .height(72.dp)
                .combinedClickable(
                    onClick = { if (program.scheduleStart.getStreamType(program.scheduleEnd) == StreamType.Live) {
                        onProgramClicked(program.channel?.channelid.orEmpty(), program.taxonomies?.first()?.taxonomyId.orEmpty())
                    } },
                    onLongClick = { onLongPressedCallback(program) },
                )
        ) {
            ChannelMetaDataView(
                channelName = program.channel?.title.orEmpty(),
                streamType = program.scheduleStart.getStreamType(program.scheduleEnd),
                broadCastTime = program.getFormattedScheduledTime()
            )
        }
    }
}
