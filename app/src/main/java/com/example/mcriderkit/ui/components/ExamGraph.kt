package com.example.mcriderkit.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.ui.NonProExamViewModel
import com.example.mcriderkit.ui.ProExamViewModel
import com.example.mcriderkit.ui.StudentExamViewModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties.Rotation

@Composable
fun ExamGraph(
    studentViewModel: StudentExamViewModel,
    nonProViewModel: NonProExamViewModel,
    proViewModel: ProExamViewModel
) {

    LaunchedEffect(Unit) {
        studentViewModel.fetchHighestScore("Student Exam")
        nonProViewModel.fetchHighestScore("Non-professional Exam")
        proViewModel.fetchHighestScore("Professional Exam")
    }

    // Collect quiz scores from each view model
    val studentScore by studentViewModel.highestScore.collectAsState()
    val nonProScore by nonProViewModel.highestScore.collectAsState()
    val proScore by proViewModel.highestScore.collectAsState()

    // Prepare quiz score data
    val quizScores = listOf(
        "Student Exam" to (studentScore ?: 0),
        "Non-professional Exam" to (nonProScore ?: 0),
        "Professional Exam" to (proScore ?: 0)
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quiz Scores",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (quizScores.any { it.second > 0 }) {
                QuizBarChart(quizScores)
            } else {
                Text("No data available")
            }
        }
    }
}

@Composable
fun QuizBarChart(quizScores: List<Pair<String, Int>>) {
    // Prepare bar chart data
    val barData = remember(quizScores) {
        quizScores.map { (label, score) ->
            Bars(
                label = label,
                values = listOf(
                    Bars.Data(
                        value = score.toDouble(),
                        color = Brush.verticalGradient(
                            colors = listOf(Color(0xFF2196F3), Color(0xFF1976D2)), // Blue gradient
                            startY = 0.0f,
                            endY = 500.0f
                        )
                    )
                )
            )
        }
    }

    // Display the bar chart
    ColumnChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        data = barData,
        labelProperties = LabelProperties(
            rotation = Rotation(
                degree = 0f
            ),
            enabled = true,
            labels = listOf("Student","Non-professional","Professional")
        ),
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = 2.dp,
            thickness = 60.dp
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false)
    )
}
