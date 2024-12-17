package com.example.mcriderkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mcriderkit.data.QuizDatabase
import com.example.mcriderkit.ui.ExamViewModel
import com.example.mcriderkit.ui.components.QuizRepository
import com.example.mcriderkit.ui.theme.MCRiderKitTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repository
        val database = QuizDatabase.getDatabase(applicationContext)
        val repository = QuizRepository(database.quizScoreDao())
        val viewModel = ExamViewModel(repository)

        enableEdgeToEdge()
        setContent {

            MCRiderKitTheme {
                NavigationApp(viewModel = viewModel)
                }
            }
        }
    }
