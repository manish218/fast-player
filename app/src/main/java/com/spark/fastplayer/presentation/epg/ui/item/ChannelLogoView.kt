package com.spark.fastplayer.presentation.epg.ui.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.spark.fastplayer.R
import com.spark.fastplayer.presentation.epg.ui.EPGCardItemSurface

@Composable
fun ChannelLogoView(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    EPGCardItemSurface(
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
