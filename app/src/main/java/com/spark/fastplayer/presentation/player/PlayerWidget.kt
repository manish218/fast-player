package com.spark.fastplayer.presentation.player

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.spark.fastplayer.R
import com.spark.fastplayer.common.noRippleClickable

@SuppressLint("ResourceAsColor")
@Composable
fun VideoPlayerWidget(playbackState: PlaybackState) {

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build().also {
                it.prepare()
            }
    }

    when (playbackState) {
        is PlaybackState.PlaybackSuccess -> {
            RenderPlayerView(exoPlayer, playbackState)
            exoPlayer.setMediaItem(MediaItem.Builder()
                .apply {
                    setUri(playbackState.metData.streamUrl)
                    setMediaMetadata(
                        MediaMetadata.Builder().build()
                    )
                }
                .build())
            exoPlayer.prepare()
            exoPlayer.playWhenReady = true
            exoPlayer.play()
        }
        is PlaybackState.Init -> {
            RenderPlayerView(exoPlayer, playbackState)
        }
        else -> { }
    }

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    Box(modifier = Modifier) {
        DisposableEffect(key1 = Unit) {

            val observer = LifecycleEventObserver { owner, event ->
                when (event) {
                    Lifecycle.Event.ON_STOP -> {
                        exoPlayer.stop()
                    }
                    Lifecycle.Event.ON_START -> {
                        exoPlayer.playWhenReady = true
                    }
                    else -> {

                    }
                }
            }
            val lifecycle = lifecycleOwner.value.lifecycle
            lifecycle.addObserver(observer)

            onDispose {
                exoPlayer.release()
                lifecycle.removeObserver(observer)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RenderPlayerView(exoPlayer: ExoPlayer, playbackState: PlaybackState) {
    var shouldShowControls by remember { mutableStateOf(false) }
    val context = LocalContext.current

    ConstraintLayout {
        val (playerView, OverlayView) = createRefs()
        AndroidView(
            modifier = Modifier
                .clickable {
                    shouldShowControls = shouldShowControls.not()
                }
                .background(MaterialTheme.colorScheme.primary)
                .constrainAs(playerView) {},
            factory = {
                StyledPlayerView(context).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setShowBuffering(StyledPlayerView.SHOW_BUFFERING_ALWAYS)
                }
            }
        )

        AnimatedVisibility(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(OverlayView) {
                    top.linkTo(playerView.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                },
            visible = shouldShowControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                .fillMaxSize()) {
               if(playbackState is PlaybackState.PlaybackSuccess) {
                   TopControl(
                       modifier = Modifier
                           .align(Alignment.TopStart)
                           .fillMaxWidth()
                           .animateEnterExit(
                               enter = slideInVertically(
                                   initialOffsetY = { fullHeight: Int ->
                                       fullHeight
                                   }
                               ),
                               exit = slideOutVertically(
                                   targetOffsetY = { fullHeight: Int ->
                                       fullHeight
                                   }
                               )
                           ),
                       playBackMetaData = playbackState.metData
                   )
               }
                BottomControls(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .fillMaxWidth()
                        .animateEnterExit(
                            enter = slideInVertically(
                                initialOffsetY = { fullHeight: Int ->
                                    fullHeight
                                }
                            ),
                            exit = slideOutVertically(
                                targetOffsetY = { fullHeight: Int ->
                                    fullHeight
                                }
                            )
                        )
                )
            }
        }
    }
}

@Composable
private fun TopControl(modifier: Modifier = Modifier, playBackMetaData: PlayBackMetaData?) {

    Row(modifier = Modifier.padding(top = 16.dp, start = 16.dp)) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(playBackMetaData?.channelLogoUrl)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Inside,
            placeholder = painterResource(R.drawable.channel_icon),
            modifier = Modifier
                .height(40.dp)
                .width(60.dp),
            contentDescription = "contentDescription",
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
        ) {

            Text(
                modifier = modifier.padding(start = 16.dp),
                text = playBackMetaData?.title.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = modifier.padding(start = 16.dp),
                text = playBackMetaData?.description.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(bottom = 12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            trackColor = MaterialTheme.colorScheme.onPrimary
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {

            Box(modifier = Modifier.noRippleClickable(onClick = {  /*do action on fav button*/ })) {
                Image(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.baseline_heart_broken_24),
                    contentDescription = "like"
                )
            }

            Box(modifier = Modifier.noRippleClickable(onClick = {   /*do action on share button */ })) {
                Image(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.baseline_share_24),
                    contentDescription = "share"
                )
            }

            Box(modifier = Modifier.noRippleClickable(onClick = { /* do action on info button */ })) {
                Image(
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                    contentScale = ContentScale.Crop,
                    painter = painterResource(id = R.drawable.baseline_info_24),
                    contentDescription = "info"
                )
            }
        }
    }
}
