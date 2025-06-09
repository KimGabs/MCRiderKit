package com.example.mcriderkit.ui

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import com.example.mcriderkit.R
import com.example.mcriderkit.data.HazardTest
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun HazardTestScreenReview(
    video: HazardTest,
    onBackPressed: () -> Unit,
    onReviewFinished: () -> Unit,
    modifier: Modifier = Modifier
){
    var currentPosition by remember { mutableFloatStateOf(0f) }
    var startTime by remember { mutableStateOf<Long?>(null) }
    var isVideoStarted by remember { mutableStateOf(false) }
    var isVideoEnded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    // Simulate loading state
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1500)  // Simulate a delay for loading
            isLoading = false // After loading, set isLoading to false
        }
    }
    // Once loading is complete, set start time
    LaunchedEffect(isLoading, isVideoStarted) {
        if (!isLoading && isVideoStarted) {
            delay(350)
            startTime = System.currentTimeMillis()  // Set start time when loading is complete
        }
    }

    // Update the current position of the vertical line based on elapsed time
    LaunchedEffect(startTime) {
        if (startTime != null) {
            while (true) {
                val elapsedTime = (System.currentTimeMillis() - startTime!!) / 1000f // Time in seconds
                currentPosition = elapsedTime / video.videoLength

                // Cap the position to 1.0 (end of the video duration)
                if (currentPosition > 1f) {
                    currentPosition = 1f
                }

                delay(50) // Update every 50ms for smooth movement
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.Black)
                ) {
                    VideoPlayer(
                        video = video,
                        onVideoEnded = { isVideoEnded = true },
                        modifier = Modifier.fillMaxSize(),
                        onVideoStarted = { isVideoStarted = true }
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(((video.perfectRange - video.earlyRange) * 355).dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (video.earlyRange * 350).dp)
                            .background(colorResource(R.color.green))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(((video.goodRange - video.perfectRange) * 355).dp)
                            .align(Alignment.CenterStart)
                            .offset(x = (video.perfectRange * 350).dp)
                            .background(colorResource(R.color.lightGreen))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(((video.lateRange - video.goodRange) * 350).dp)
                            .offset(x = (video.goodRange * 350).dp)
                            .background(colorResource(R.color.lightYellow))
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = (currentPosition * 390).dp)
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(Color.Red)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .border(2.dp, Color.Black)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        val scoreRanges = listOf(
                            Pair(R.color.green, " 100 Points"),
                            Pair(R.color.lightGreen, " 75 Points"),
                            Pair(R.color.lightYellow, " 50 Points")
                        )
                        scoreRanges.forEach { (color, label) ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .background(colorResource(color))
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = isVideoEnded,
                enter = fadeIn(animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(500))
            ) {
                Button(
                    onClick = { onReviewFinished() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Continue")
                }
            }
        }
    }

}


