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
            delay(350)  // Simulate a delay for loading
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

            Spacer(modifier = Modifier.height(32.dp))

            // Video Player with Black Background Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Adjust the height as per the desired video size
                    .background(Color.Black)
            ) {
                VideoPlayer(
                    video = video,
                    onVideoEnded = { isVideoEnded = true },
                    modifier = Modifier.fillMaxSize(),
                    onVideoStarted = { isVideoStarted = true }
                )
            }

            // Box to contain the scoring areas (located at the bottom of the screen)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp) // Height of the flag container (adjustable)
                    .background(Color.White) // Background for flag box
            ) {
                // Perfect score range (Green)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(((video.perfectRange - video.earlyRange) * 390).dp) // Adjust width for 20% of total width (relative to video length)
                        .align(Alignment.CenterStart)
                        .offset(x = (video.earlyRange * 390).dp) // Offset for the start of the perfect range
                        .background(colorResource(R.color.green))
                )

                // Good score range (lightGreen)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(((video.goodRange - video.perfectRange) * 390).dp) // Adjust width for 20% of total width (relative to video length)
                        .align(Alignment.CenterStart)
                        .offset(x = (video.perfectRange * 390).dp) // Offset for the start of the perfect range
                        .background(colorResource(R.color.lightGreen))
                )

                // Mid Range (Light Yellow)
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(((video.lateRange - video.goodRange) * 390).dp) // Width based on range difference
                        .offset(x = (video.goodRange * 390).dp) // Start of the good range
                        .background(colorResource(R.color.lightYellow)) // Good range color
                )

                // Draw the moving vertical line
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart) // Align to the left side of the Box
                        .offset(x = (currentPosition * 390).dp) // Move the line based on currentPosition (0.0 to 1.0)
                        .fillMaxHeight() // Make the line take up the full height of the box
                        .width(2.dp) // Line width
                        .background(Color.Red) // Line color
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .border(2.dp, Color.Black)){

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    // First row: Green Box and text
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp) // Add spacing between rows
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp) // Size of the colored box
                                .background(colorResource(R.color.green)) // Color of the box
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between box and text
                        Text(
                            text = " 100 Points",
                            style = MaterialTheme.typography.bodySmall // Text style
                        )
                    }

                    // Second row: Light Green Box and text
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp) // Size of the colored box
                                .background(colorResource(R.color.lightGreen))
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between box and text
                        Text(
                            text = " 75 Points",
                            style = MaterialTheme.typography.bodySmall // Text style
                        )
                    }

                    // Third row: Light Yellow Box and text
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp) // Size of the colored box
                                .background(colorResource(R.color.lightYellow))
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // Space between box and text
                        Text(
                            text = " 50 Points",
                            style = MaterialTheme.typography.bodySmall // Text style
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = isVideoEnded,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)),
            modifier = Modifier.align(Alignment.CenterHorizontally)){
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


