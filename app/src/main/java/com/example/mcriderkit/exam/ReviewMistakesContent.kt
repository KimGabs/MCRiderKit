package com.example.mcriderkit.exam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mcriderkit.data.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewMistakesContent(
    questions: List<Question>,
    selectedAnswers: Map<Int, Int>,
    onBackToResults: () -> Unit
) {
    // Correctly identify only the questions the user got wrong
    val mistakes = questions.filterIndexed { index, question ->
        selectedAnswers[index] != question.answerIndex
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Review Mistakes (${mistakes.size})") },
                navigationIcon = {
                    IconButton(onClick = onBackToResults) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp) // Adds breathing room around the list
        ) {
            items(mistakes) { question ->
                // Look up the specific choice the user made for this question
                val originalIndex = questions.indexOf(question)
                val userChoiceIndex = selectedAnswers[originalIndex]

                MistakeCard(question, userChoiceIndex)
            }
        }
    }
}

@Composable
fun MistakeCard(question: Question, userChoiceIndex: Int?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Question Text
            Text(text = question.text, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)

            // 2. Image Support (Important for Road Signs!)
            if (!question.imageUrl.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = question.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. User's Wrong Answer (Red)
            val userChoiceText = question.choices.getOrNull(userChoiceIndex ?: -1) ?: "No answer provided"
            Surface(
                color = Color(0xFFFFEBEE), // Very light red
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "✗ Your Answer: $userChoiceText",
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. Correct Answer (Green)
            val correctChoiceText = question.choices.getOrNull(question.answerIndex) ?: ""
            Surface(
                color = Color(0xFFE8F5E9), // Very light green
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "✓ Correct Answer: $correctChoiceText",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier.padding(8.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // 5. Explanation
            if (!question.explanation.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Why: ${question.explanation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

