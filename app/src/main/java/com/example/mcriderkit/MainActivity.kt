package com.example.mcriderkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mcriderkit.data.QuizDatabase
import com.example.mcriderkit.ui.ExamViewModel
import com.example.mcriderkit.ui.HazardTestViewModel
import com.example.mcriderkit.ui.components.HazardRepository
import com.example.mcriderkit.ui.components.QuizRepository
import com.example.mcriderkit.ui.theme.MCRiderKitTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch() {
            delay(4000)
            keepSplashScreen = false
        }
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
