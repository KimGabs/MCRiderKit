package com.example.mcriderkit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.data.ExamUiState
import com.example.mcriderkit.ui.components.BaseExamViewModel
import kotlinx.coroutines.delay


@Composable
fun ExamResultScreen(
    viewModel: BaseExamViewModel,
    onRetry: () -> Unit,
    onMainMenu: () -> Unit,
) {

    val highestScore by viewModel.highestScore.collectAsState(initial = null)

    // Fetch highest score when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchHighestScore("Non-Pro")
    }

    val examState by viewModel.examState.collectAsState(initial = ExamUiState())
    var isLoading by remember { mutableStateOf(false) } // Loading state

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
            // Title
            Text(
                text = "Quiz Completed!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score
            Text(
                text = "Your Score: ${examState.score} / ${examState.totalQuestions}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.secondary
            )


            if (highestScore != null) {
                Text("Highest Score: ${highestScore!!}", style = MaterialTheme.typography.titleMedium)
            } else {
                Text("No high score yet!", style = MaterialTheme.typography.titleMedium)
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
}

