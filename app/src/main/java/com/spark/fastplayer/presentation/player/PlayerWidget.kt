package com.spark.fastplayer.presentation.player

import android.annotation.SuppressLint
import android.app.Activity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.spark.fastplayer.R


@SuppressLint("ResourceAsColor")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VideoPlayerWidget(
    videoUri: String,
    viewModel: PlayerViewModel
) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                setMediaItem(
                    MediaItem.Builder()
                        .apply {
                            setUri(videoUri)
                            setMediaMetadata(
                                MediaMetadata.Builder()
                                    .setDisplayTitle("Streaming Kids Video")
                                    .build()
                            )
                        }
                        .build()
                )
                prepare()
                playWhenReady = true
            }
    }

    var shouldShowControls by remember { mutableStateOf(false) }

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    Box(modifier = Modifier) {
        DisposableEffect(key1 = Unit) {

            val observer = LifecycleEventObserver { owner, event ->
                when (event) {
                    Lifecycle.Event.ON_PAUSE -> {
                        exoPlayer.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        exoPlayer.play()
                    }
                    else -> {
                        exoPlayer.pause()
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

        ConstraintLayout {
            val (playerView, OverlayView) = createRefs()

            AndroidView(
                modifier = Modifier
                    .clickable {
                        shouldShowControls = shouldShowControls.not()
                    }
                    .constrainAs(playerView) {
                    },
                factory = {
                    StyledPlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        layoutParams = FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
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
                Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.7f))) {
                    TopControl(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth(),
                        title = { exoPlayer.mediaMetadata.displayTitle.toString() }
                    )

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
                            ),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    title: () -> String,
    viewModel: PlayerViewModel,
    exoPlayer: ExoPlayer
) {
    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.7f))) {
            TopControl(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth(),
                title = title
            )

            BottomControls(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun TopControl(modifier: Modifier = Modifier, title: () -> String) {
    val videoTitle = remember(title()) { title() }

    Image(
        modifier = Modifier.padding(start = 16.dp, top = 26.dp),
        contentScale = ContentScale.Crop,
        painter = painterResource(id = R.drawable.channel_icon),
        contentDescription = "channelLogo"
    )

    Text(
        modifier = modifier.padding(start = 16.dp, top = 88.dp),
        text = videoTitle,
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun BottomControls(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel
) {

    Column(modifier = modifier.padding(bottom = 12.dp)) {
        Box(modifier = Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    color = Color.White,
                    trackColor = Color.White
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "12:25",
                color = Color.White
            )
        }
    }
}