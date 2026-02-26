@file:kotlin.OptIn(ExperimentalMaterial3Api::class)

package com.example.mcriderkit.hpt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import com.example.mcriderkit.R
import com.example.mcriderkit.data.HazardClip
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

@SuppressLint("DiscouragedApi")
@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun HazardReviewScreen(
    clipId: String,
    userTapTime: Long,
    navController: NavHostController
) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val view = LocalView.current

    var clip by remember { mutableStateOf<HazardClip?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var playbackProgress by remember { mutableStateOf(0f) }
    var videoDuration by remember { mutableStateOf(1L) } // Default to 1 to avoid div by zero

    var resId by remember { mutableStateOf(0) }

    LaunchedEffect(clipId) {
        if (clipId.isNotBlank()) {
            val docRef = db.collection("hazard_clips").document(clipId)
            try {
                val document = docRef.get().await()
                if (document != null) {
                    clip = document.toObject(HazardClip::class.java)
                    clip?.let {
                        resId = context.resources.getIdentifier(
                            it.videoFileName,
                            "raw",
                            context.packageName
                        )
                    }
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
        isLoading = false
    }

    val exoPlayer = remember(resId) {
        if (resId == 0) return@remember null
        ExoPlayer.Builder(context).build().apply {
            val uri = RawResourceDataSource.buildRawResourceUri(resId)
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
        }
    }

    LaunchedEffect(exoPlayer) {
        exoPlayer?.let {
            // Wait until the player is ready to get a valid duration
            while (it.duration <= 0) {
                delay(100)
            }
            videoDuration = it.duration

            while (true) {
                playbackProgress = it.currentPosition.toFloat() / videoDuration.toFloat()
                delay(100)
            }
        }
    }

    DisposableEffect(Unit) {
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
            exoPlayer?.release()
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    if (isLoading || exoPlayer == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Review Hazard") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.Close, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.Black)
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

                // Scoring Window and User Tap visualization
                clip?.let { hazard ->
                    if (videoDuration > 1L) {
                        BoxWithConstraints(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 40.dp) // Added padding so it's not too low
                        ) {
                            val localMaxWidth = maxWidth
                            val windowStartProgress = hazard.windowStart.toFloat() / videoDuration.toFloat()
                            val windowEndProgress = hazard.windowEnd.toFloat() / videoDuration.toFloat()
                            val userTapProgress = userTapTime.toFloat() / videoDuration.toFloat()

                            // Wrap everything in a Column to stack the flag ABOVE the bars
                            Column(modifier = Modifier.fillMaxWidth()) {

                                // 1. THE FLAG (Sits on top)
                                Image(
                                    painter = painterResource(id = R.drawable.hazard_flag),
                                    contentDescription = "User Tap Flag",
                                    modifier = Modifier
                                        // coerceIn ensures the flag stays within screen bounds (0.0 to 1.0)
                                        .offset(x = (localMaxWidth * userTapProgress.coerceIn(0f, 1f)) - 8.dp)
                                        .size(24.dp)
                                )

                                // 2. THE SCORING WINDOWS (Sits below the flag)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp) // Increased height slightly
                                        .background(Color.Gray.copy(alpha = 0.3f))
                                ) {
                                    // The coloured score zones
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            // Calculate width as the difference between end and start
                                            .fillMaxWidth(fraction = (windowEndProgress - windowStartProgress).coerceIn(0f, 1f))
                                            // Shift the box to the start point
                                            .offset(x = localMaxWidth * windowStartProgress)
                                    ) {
                                        Row(modifier = Modifier.fillMaxSize()) {
                                            val colors = listOf(Color.Green, Color(0xFF9CCC65), Color.Yellow, Color(0xFFFFA726), Color.Red)
                                            colors.forEach { color ->
                                                Box(modifier = Modifier.fillMaxHeight().weight(1f).background(color))
                                            }
                                        }
                                    }

                                    // Overlay the playback progress on the same bar
                                    LinearProgressIndicator(
                                        progress = { playbackProgress },
                                        modifier = Modifier.fillMaxSize(),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = Color.Transparent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

