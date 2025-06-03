package com.example.mcriderkit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.mcriderkit.R
import com.example.mcriderkit.data.ExamUiState
import com.example.mcriderkit.ui.components.BaseExamViewModel
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
import androidx.activity.compose.BackHandler

@Composable
fun ExamResultScreen(
    viewModel: BaseExamViewModel,
    onRetry: () -> Unit,
    onMainMenu: () -> Unit,
) {
    BackHandler(enabled = true) {
        onMainMenu()  // Navigate to main menu
    }

    val highestScore by viewModel.highestScore.collectAsState(initial = null)

    // Fetch highest score when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchHighestScore("Student Exam")
    }

    val examState by viewModel.examState.collectAsState(initial = ExamUiState())
    var isLoading by remember { mutableStateOf(false) } // Loading
    val showTrophyDialog by viewModel.showTrophyDialog.collectAsState()
    val percentScore = 100 * (examState.score.toFloat() / examState.totalQuestions.toFloat())

    val badgeRes = when {
        percentScore >= 100 -> R.drawable.badge_veteran
        percentScore >= 75 -> R.drawable.badge_expert
        percentScore >= 50 -> R.drawable.badge_rookie
        else -> null
    }

    LaunchedEffect(examState.score) {
        viewModel.examShowTrophyDialog(examState.score, examState.totalQuestions)
    }
    // Show loading indicator when navigating or resetting quiz
    LaunchedEffect(isLoading) {
        if (isLoading) {
            // Simulate a delay (e.g., network call or state reset)
            delay(1500)  // Add a delay to simulate loading (you can remove it in real scenario)
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()  // Loading spinner
        } else {
            badgeRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Achievement Badge",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )
            }
            // Title
            Text(
                text = "Quiz Completed!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score
            Text(
                text = "Your Score: ${examState.score} / ${examState.totalQuestions}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                percentScore >= 100f -> {
                    // Perfect Score
                    Text("You got a perfect score! You're ready for the real thing!",
                        style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                }
                percentScore >= 90f -> {
                    // High score (90% or higher)
                    Text("This is a strong showing, but some additional studying will help you earn points on the real exam.",
                        style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                }
                percentScore >= 70f -> {
                    // Medium score (70% - 89%)
                    Text("You're so close! Keep working on the areas you're missing and fill in those gaps.",
                        style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                }
                else -> {
                    // Low score (below 70%)
                    Text("You have room for improvement. Keep practicing and focus on the areas where you struggled.",
                        style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(horizontal = 16.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Retry Button
            Button(
                onClick = {
                    isLoading = true  // Start loading
                    onRetry()  // Reset quiz state
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Retry Quiz")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Menu Button
            Button(
                onClick = {
                    isLoading = true  // Start loading
                    onMainMenu()  // Navigate to main menu
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text(text = "Return to Main Menu")
            }
        }
    }
    if (showTrophyDialog) {
        TrophyAchievementDialog(onDismiss = {
            viewModel.hideTrophyDialog()
        })
    }
}

@Composable
fun TrophyAchievementDialog(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)) // semi-transparent background
            .clickable(onClick = onDismiss, indication = null, interactionSource = remember { MutableInteractionSource() })
    ) {
        // 🎉 Confetti at top
        KonfettiView(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f),
            parties = listOf(
                Party(
                    speed = 10f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    angle = Angle.RIGHT - 45,
                    spread = Spread.SMALL,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
                    position = Position.Relative(0.0, 0.5)
                ),
                Party(
                    speed = 10f,
                    maxSpeed = 30f,
                    damping = 0.9f,
                    angle = Angle.LEFT + 45,
                    spread = Spread.SMALL,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
                    position = Position.Relative(1.0, 0.5)
                ),

            )
        )

        // 🪧 Custom Dialog Box
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .zIndex(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
                .widthIn(max = 300.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Congratulations!",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.trophy_icon),
                    contentDescription = "Trophy",
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "You've achieved a perfect score!",
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text("OK")
                }
            }
        }
    }
}


