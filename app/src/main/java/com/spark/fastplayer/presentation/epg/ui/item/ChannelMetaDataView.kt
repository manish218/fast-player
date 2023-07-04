package com.spark.fastplayer.presentation.epg.ui.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spark.fastplayer.presentation.epg.StreamType
import com.spark.fastplayer.presentation.epg.ui.EPGCardItemSurface
import com.spark.fastplayer.ui.theme.CurrentlyPlayedProgramColor
import com.spark.fastplayer.ui.theme.LiveBadgeColor
import com.spark.fastplayer.ui.theme.UpcomingBadgeColor

@Composable
fun ChannelMetaDataView(
    isCardCurrentPlayed: Boolean,
    channelName: String,
    broadCastTime: String,
    streamType: StreamType = StreamType.None,
) {
    EPGCardItemSurface(
        color = MaterialTheme.colorScheme.secondary,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isCardCurrentPlayed) colorResource(id = CurrentlyPlayedProgramColor) else MaterialTheme.colorScheme.secondary
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start)
                .padding(start = 10.dp, bottom = 10.dp)) {
                Text(
                    text = channelName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 8.dp)
            ) {
                Text(
                    text = broadCastTime,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Box(modifier  = Modifier
                    .background(
                        shape = RoundedCornerShape(3.dp),
                        color = if (streamType is StreamType.Live) LiveBadgeColor else UpcomingBadgeColor
                    )
                    .padding(1.dp)) {
                    Text(
                        text = if (streamType is StreamType.Live) " Live " else " UpComing ",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
