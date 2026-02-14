package com.example.mcriderkit.exam

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

data class QuizType(val title: String, val desc: String, val targetCategory: String, val icon: ImageVector)

@Composable
fun QuizMenuScreen(navController: NavHostController) {
    val quizTypes = listOf(
        QuizType("Full Mock Exam", "50 random questions (All topics)", "All", Icons.Default.Info),
        QuizType("Road Signs", "Practice specifically for signs", "Road Signs", Icons.Default.Info),
        QuizType("General Knowledge", "Laws, rules, and etiquette", "General Knowledge", Icons.Default.Info),
        QuizType("Fines & Penalties", "Memorize violations and costs", "Fines and Penalties", Icons.Default.Info),
        QuizType("Driving", "Practice Theoretical Driving Scenarios", "Driving", Icons.Default.Info),
        QuizType("Emergencies", "Practice emergency procedures", "Emergencies", Icons.Default.Info),
        QuizType("Road Courtesy", "Practice driving safely", "Road Courtesy", Icons.Default.Info)
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Select Quiz Type", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(quizTypes) { quiz ->
                Card(
                    onClick = { navController.navigate("quiz/${quiz.targetCategory}") },
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(quiz.icon, contentDescription = null, tint = Color(0xFF2A6CF6), modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(quiz.title, fontWeight = FontWeight.Bold)
                            Text(quiz.desc, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

