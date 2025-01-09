package com.example.mcriderkit.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import com.example.mcriderkit.R
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.mcriderkit.data.HazardTest
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun HazardTestScreen(
    video: HazardTest,
    onBackPressed: () -> Unit,
    onTestFinished: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    var flagPositions by remember { mutableStateOf(listOf<Float>()) } // Store relative positions
    var startTime by remember { mutableStateOf<Long?>(null) }
    var isVideoEnded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) } // Manage loading state

    // Simulate loading state
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1500)  // Simulate a delay for loading
            isLoading = false // After loading, set isLoading to false
        }
    }
    // Once loading is complete, set start time
    LaunchedEffect(isLoading) {
        if (!isLoading) {
            startTime = System.currentTimeMillis()  // Set start time when loading is complete
        }
    }


    // Column to arrange video and flags vertically
    Column(
        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Top // Keep video at the top
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        }
        else{
            // Back Button
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .size(48.dp)
                    .clickable { onBackPressed() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            // Video Player with Black Background Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Adjust the height as per the desired video size
                    .background(Color.Black)
            ) {
                VideoPlayer(video = video, onVideoEnded = { isVideoEnded = true }, modifier = Modifier.fillMaxSize())
            }

            // Box to contain the flags (located at the bottom of the screen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp) // Height of the flag container (adjustable)
                    .background(Color.White) // Background for flag box
            ) {

                // Display Flag Markers below the video at the bottom section
                flagPositions.forEach { position ->
                    FlagMarker(relativePosition = position)
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Detection area height
                    .background(Color.LightGray)
                    .pointerInput(Unit) {
                        detectTapGestures { tapOffset ->
                            val currentTime = System.currentTimeMillis()

                            if (startTime == null) {
                                startTime = currentTime
                            } else {
                                val elapsedTime = currentTime - startTime!!

                                // Map the tap position to the video timeline relative to the hazard's start time
                                val relativePosition = elapsedTime.toFloat() / (video.videoLength * 1000f)

                                // Ensure the user flags after the hazard start time (8 seconds)
                                if (relativePosition in 0.0..1.0) {
                                    flagPositions = flagPositions + relativePosition
                                }
                            }
                        }
                    }
            )

            // Finish Test Button (appears after video ends)
            if (isVideoEnded) {
                Button(
                    onClick = { onTestFinished(flagPositions.size) },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                ) {
                    Text("Finish Test")
                }
            }
        }
    }

}

@Composable
fun FlagMarker(relativePosition: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .offset(x = (relativePosition * 350).dp) // Map relative position to screen space (x-axis)
            .size(40.dp) // Size of the flag marker (adjust as needed)
    ) {
        Image(
            painter = painterResource(id = R.drawable.flag_icon), // PNG flag image from drawable folder
            contentDescription = "Flag Marker",
            modifier = Modifier.fillMaxSize() // Fill the Box with the PNG image
        )
    }
}


@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(video: HazardTest, onVideoEnded: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val videoUri = "android.resource://${context.packageName}/raw/${video.videoPath}"

    // Initialize ExoPlayer
    val exoPlayer = remember { ExoPlayer.Builder(context).build().apply {
        setMediaItem(MediaItem.fromUri(Uri.parse(videoUri)))
        prepare()
        playWhenReady = true  // Automatically start video
        // Add listener to detect when video has ended
        addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    onVideoEnded() // Notify that the video has ended
                }
            }
        })
    }}

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    // Render the PlayerView
    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false  // Remove player controls
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
            }
        }
    )
}
