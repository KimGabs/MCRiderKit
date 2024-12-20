package com.example.mcriderkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mcriderkit.data.QuizDatabase
import com.example.mcriderkit.ui.ExamViewModel
import com.example.mcriderkit.ui.HazardTestViewModel
import com.example.mcriderkit.ui.components.HazardRepository
import com.example.mcriderkit.ui.components.QuizRepository
import com.example.mcriderkit.ui.theme.MCRiderKitTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = QuizDatabase.getDatabase(applicationContext)
        val quizRepository = QuizRepository(database.quizScoreDao())
        val hazardRepository = HazardRepository(database.hazardTestDao())

        val quizViewModel = ExamViewModel(quizRepository)
        val hazardViewModel = HazardTestViewModel(hazardRepository)

        enableEdgeToEdge()
        setContent {

            MCRiderKitTheme {
                NavigationApp(quizViewModel = quizViewModel, hazardViewModel = hazardViewModel)
                }
            }
        }
    }
