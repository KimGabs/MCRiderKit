package com.example.mcriderkit.exam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.data.Question
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ResultSummary(
    questions: List<Question>,
    selectedAnswers: Map<Int, Int>,
    onReviewMistakes: () -> Unit, // New: Trigger for Review Phase
    onExit: () -> Unit            // New: Trigger to go back to Home
) {
    val userId = Firebase.auth.currentUser?.uid

    // 1. Calculate Score
    val score = questions.filterIndexed { index, question ->
        selectedAnswers[index] == question.answerIndex
    }.size

    val total = questions.size
    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0
    val isPassed = percentage >= 80 // LTO Standard

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Visual Result
        Icon(
            imageVector = if (isPassed) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = if (isPassed) Color(0xFF2E7D32) else Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isPassed) "EXAM PASSED" else "EXAM FAILED",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = if (isPassed) Color(0xFF2E7D32) else Color.Red
        )

        Text(
            text = "$score / $total ($percentage%)",
            style = MaterialTheme.typography.titleLarge,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        // --- BUTTONS ---

        // 1. Review Mistakes (Only show if there are mistakes)
        if (score < total) {
            Button(
                onClick = onReviewMistakes,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Review Mistakes")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. Back to Dashboard
        OutlinedButton(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Dashboard")
        }
    }
}
