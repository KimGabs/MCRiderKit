package com.example.mcriderkit.hpt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.example.mcriderkit.R
import com.example.mcriderkit.data.HazardClip
import com.example.mcriderkit.data.updateHazardStreakAndSave
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay

@SuppressLint("DiscouragedApi")
@OptIn(UnstableApi::class)
@Composable
fun HazardPlayerScreen(resId: Int, isDaily: Boolean, navController: NavHostController) {
    val context = LocalContext.current
    val db = Firebase.firestore

    var currentClip by remember { mutableStateOf<HazardClip?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var highestScore by remember { mutableIntStateOf(0) }
    var clickCount by remember { mutableIntStateOf(0) }
    var tapPositions by remember { mutableStateOf(listOf<Float>()) }
    var bestTapTime by remember { mutableStateOf(0L) }

    val view = LocalView.current

    var showExitDialog by remember { mutableStateOf(false) }

    LaunchedEffect(resId) {
        db.collection("hazard_clips").get().addOnSuccessListener { result ->
            val allClips = result.toObjects(HazardClip::class.java)
            currentClip = allClips.find { clip ->
                context.resources.getIdentifier(clip.videoFileName, "raw", context.packageName) == resId
            }
            isLoading = false
        }
    }

    var hasSaved by remember { mutableStateOf(false) }
    var isVideoEnded by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableFloatStateOf(0f) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = RawResourceDataSource.buildRawResourceUri(resId)
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    // 1. Guard against multiple triggers
                    if (state == Player.STATE_ENDED && !isVideoEnded) {
                        isVideoEnded = true
                    }
                }
            })
        }
    }

    LaunchedEffect(exoPlayer) {
        while(true) {
            playbackProgress = exoPlayer.currentPosition.toFloat() / exoPlayer.duration.coerceAtLeast(1L).toFloat()
            delay(100)
        }
    }

    LaunchedEffect(isVideoEnded) {
        if (isVideoEnded && !hasSaved) {
            val userId = Firebase.auth.currentUser?.uid
            val clipId = currentClip?.id ?: ""

            if (userId != null) {
                hasSaved = true

                // 🚀 The function below now handles BOTH history and streak saving
                updateHazardStreakAndSave(
                    userId = userId,
                    clipId = clipId,
                    highestScore = highestScore,
                    bestTapTime = bestTapTime,
                    isDaily = isDaily
                )

                // Navigate immediately
                navController.navigate("hazard_result/$highestScore/$clipId/$bestTapTime/$isDaily") {
                    popUpTo("hazard_player") { inclusive = true }
                }
            }
        }
    }

    // 3. Properly release the player and reset orientation
    DisposableEffect(exoPlayer) {
        val window = (view.context as Activity).window
        val insetsController = WindowCompat.getInsetsController(window, view)

        insetsController.let {
            it.hide(WindowInsetsCompat.Type.systemBars())
            it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val activity = context as? Activity
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
            exoPlayer.release() // Release player resources
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }


    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.Black)
                    .clickable {
                        clickCount++

                        val tapProgress =
                            exoPlayer.currentPosition.toFloat() / exoPlayer.duration
                                .coerceAtLeast(1L).toFloat()
                        tapPositions = tapPositions + tapProgress


                        if (clickCount > 5) {
                            highestScore = 0
                        } else {
                            val currentPos = exoPlayer.currentPosition
                            val score = calculateHptScore(
                                currentPos,
                                currentClip?.windowStart ?: 0L,
                                currentClip?.windowEnd ?: 1000L
                            )
                            if (score > highestScore) {
                                highestScore = score
                                bestTapTime = currentPos
                            }
                        }
                    }
            ) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = {
                        exoPlayer.pause()
                        showExitDialog = true
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Test",
                        tint = Color.White.copy(alpha = 0.5f),
                    )
                }

                BoxWithConstraints(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 0.dp)
                ) {
                    Text(
                        "TAP SCREEN TO IDENTIFY HAZARD",
                        color = Color.White.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 60.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                    LinearProgressIndicator(
                        progress = { playbackProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .height(8.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.White.copy(alpha = 0.3f),
                        gapSize = 0.dp
                    )
                    tapPositions.forEach { progress ->
                        Image(
                            painter = painterResource(id = R.drawable.hazard_flag),
                            contentDescription = "Tap Marker",
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                // maxWidth is from the BoxWithConstraints we set up earlier
                                .offset(x = (maxWidth * progress) - 8.dp)
                                .size(34.dp) // Adjust size so the flag is visible but not huge
                                .padding(bottom = 12.dp)
                        )
                    }
                }
            }

            if (showExitDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showExitDialog = false
                        exoPlayer.play()
                    },
                    title = { Text("Exit Test?") },
                    text = { Text("Are you sure you want to exit? Your progress will not be saved.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showExitDialog = false

                            if (isDaily) {
                                // 🚀 Navigate back to Home if it was the Daily Challenge
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            } else {
                                // 🚀 Navigate back to the Hazard Menu for practice
                                navController.navigate("hazard_menu") {
                                    popUpTo("hazard_menu") { inclusive = true }
                                }
                            }
                        }) {
                            Text("Exit Test", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showExitDialog = false
                            exoPlayer.play()
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}


fun calculateHptScore(clickTime: Long, start: Long, end: Long): Int {
    if (clickTime < start || clickTime > end) return 0 // Outside the window

    val windowDuration = end - start
    val segmentDuration = windowDuration / 5

    // Calculate how far into the window the click was
    val elapsed = clickTime - start

    return when {
        elapsed <= segmentDuration -> 5       // 1st segment
        elapsed <= segmentDuration * 2 -> 4   // 2nd segment
        elapsed <= segmentDuration * 3 -> 3   // 3rd segment
        elapsed <= segmentDuration * 4 -> 2   // 4th segment
        else -> 1                             // 5th segment
    }
}
