package com.example.mcriderkit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcriderkit.R

@Composable
fun NonProQuizScreen(
    viewModel: NonProExamViewModel = viewModel(),
    onNextButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val examState by viewModel.examState.collectAsState()
    val currentQuestion = viewModel.questions[examState.currentQuestionIndex]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress Indicator
        LinearProgressIndicator(
            progress = (examState.currentQuestionIndex + 1).toFloat() / viewModel.questions.size,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Question Text
        Text(
            text = "Question ${examState.currentQuestionIndex + 1}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = currentQuestion.question,
            style = MaterialTheme.typography.bodyLarge
        )
        // Image placeholder
        // Display image only if it exists
        currentQuestion.imageResId?.let { imageResId ->
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = "Question Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
                contentScale = ContentScale.Fit // Ensures the entire image is visible
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Display the Answer Choices
        currentQuestion.options.forEachIndexed { index, option ->
            val isSelected = index == examState.selectedAnswer
            val isCorrect = index == currentQuestion.correctAnswerIndex
            val backgroundColor = when {
                examState.hasSubmitted && isCorrect -> MaterialTheme.colorScheme.tertiary // Highlight correct answer
                isSelected -> MaterialTheme.colorScheme.secondary // Highlight selected answer
                else -> MaterialTheme.colorScheme.primary // Default
            }

            Button(
                onClick = {
                    if (!examState.hasSubmitted) {
                        viewModel.selectAnswer(index)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
            ) {
                Text(text = option,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    )
            }
        }

        // Submit Button
        if (!examState.hasSubmitted) {
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    viewModel.submitAnswer()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.submit))
            }
        } else {
            // Next Question Button
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (examState.currentQuestionIndex == viewModel.questions.size - 1) {
                        viewModel.finalQuestion(context.getString(R.string.non_pro_exam))
                        onNextButtonClicked()
                    } else {
                        viewModel.nextQuestion()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (examState.currentQuestionIndex == viewModel.questions.size - 1) {
                        "View Result"
                    } else {
                        "Next Question"
                    }
                )
            }
        }
    }
}

