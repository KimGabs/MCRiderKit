package com.example.mcriderkit.ui

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.mcriderkit.data.HazardTest
import kotlinx.coroutines.delay

@Composable
fun HazardResultScreen(
    video: HazardTest,
    onMainMenu: () -> Unit,
    onRetry: () -> Unit,
    onReview: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isLoading by remember { mutableStateOf(true) } // Manage loading state

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
            Text(
                text = "Hazard Perception Test Completed!",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your Score: ${video.lastScore} / 100",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show feedback or recommendations
            Text(
                text = when {
                    video.lastScore >= 100 -> "Perfect! You're a hazard perception master."
                    video.lastScore >= 75 -> "Great job! You're a hazard perception expert."
                    video.lastScore >= 50 -> "Good effort! Keep practicing."
                    else -> "Needs improvement. Try again!"
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
                modifier = Modifier.fillMaxWidth()
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
                    containerColor = colorResource(R.color.holo_purple)
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
}