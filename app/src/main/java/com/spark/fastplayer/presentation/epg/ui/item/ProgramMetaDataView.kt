package com.spark.fastplayer.presentation.epg.ui.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.spark.fastplayer.common.getStreamType
import com.spark.fastplayer.common.toBroadCastTime
import com.spark.fastplayer.presentation.epg.ui.EPGCardItemSurface
import org.openapitools.client.models.Program

@Composable
fun ProgramCardView(
    program: Program,
    onProgramClicked: (String) -> Unit,
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
                .clickable(onClick = { onProgramClicked(program.channel?.url.orEmpty()) }).fillMaxSize()
        ) {
            ChannelMetaDataView(
                channelName = program.channel?.title.orEmpty(),
                streamType = program.scheduleStart.getStreamType(program.scheduleEnd),
                broadCastTime = program.scheduleStart?.toBroadCastTime() + " - " + program.scheduleEnd?.toBroadCastTime()
            )
        }
    }
}
