package com.example.mcriderkit.ui

import androidx.compose.foundation.Image
import com.example.mcriderkit.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.ui.components.BaseHazardViewModel
import kotlinx.coroutines.delay

@Composable
fun HazardResultScreen(
    video: HazardTest,
    onMainMenu: () -> Unit,
    onRetry: () -> Unit,
    onReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val baseHazardViewModel: BaseHazardViewModel = viewModel()
    var isLoading by remember { mutableStateOf(true) } // Manage loading state
    val showHazardTrophyDialog by baseHazardViewModel.showHazardTrophyDialog.collectAsState()

    val badgeRes = when {
        video.lastScore >= 100 -> R.drawable.badge_veteran
        video.lastScore >= 75 -> R.drawable.badge_expert
        video.lastScore >= 50 -> R.drawable.badge_rookie
        else -> null
    }

    LaunchedEffect(video.lastScore) {
        baseHazardViewModel.hazardShowTrophyDialog(video.lastScore)
    }

    // Simulate loading state
    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(1500)  // Simulate a delay for loading
            isLoading = false // After loading, set isLoading to false
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
            CircularProgressIndicator()
        } else {
            badgeRes?.let {
                Image(
                    painter = painterResource(id = it),
                    contentDescription = "Achievement Badge",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(bottom = 16.dp)
                )
            }

            Text(
                text = "Hazard Perception Test Completed!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Score: ${video.lastScore} / 100",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show feedback or recommendations
            Text(
                text = when {
                    video.lastScore >= 100 -> "You're a Hazard Perception Veteran!"
                    video.lastScore >= 75 -> "Great job! You're a Hazard Perception Expert."
                    video.lastScore >= 50 -> "Good effort! Keep practicing."
                    else -> "You have room for improvement. Keep practicing."
                },
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Retry Button
            Button(
                onClick = {
                    isLoading = true  // Start loading
                    onRetry()  // Reset quiz state
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text(text = "Retry Test")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Review Test Button
            Button(
                onClick = {
                    isLoading = true  // Start loading
                    onReview()  // Navigate to main menu
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(text = "Review Test")
            }

            Spacer(modifier = Modifier.height(12.dp))

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
    if (showHazardTrophyDialog) {
        TrophyAchievementDialog(onDismiss = {
            baseHazardViewModel.hideTrophyDialog()
        })
    }
}