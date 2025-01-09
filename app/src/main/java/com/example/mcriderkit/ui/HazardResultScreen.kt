package com.example.mcriderkit.ui

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
import androidx.compose.material3.Button
import com.example.mcriderkit.data.HazardTest

@Composable
fun HazardResultScreen(
    video: HazardTest,
    onMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text("Your Score: ${video.lastScore}", style = MaterialTheme.typography.titleMedium)

        // Show feedback or recommendations
        Text(
            text = when {
                video.lastScore >= 80 -> "Great job! You're a hazard perception expert."
                video.lastScore >= 50 -> "Good effort! Keep practicing."
                else -> "Needs improvement. Try again!"
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Button to go back to the menu or retry the test
        Button(
            onClick = {
                onMainMenu()  // Navigate to main menu
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Back to Menu")
        }
    }
}