package com.example.mcriderkit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mcriderkit.data.QuizDatabase
import com.example.mcriderkit.ui.ExamViewModel
import com.example.mcriderkit.ui.HazardTestViewModel
import com.example.mcriderkit.ui.UserInputScreen
import com.example.mcriderkit.ui.components.HazardRepository
import com.example.mcriderkit.ui.components.QuizRepository
import com.example.mcriderkit.ui.theme.MCRiderKitTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.Context


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        var keepSplashScreen = true

        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch() {
            delay(3000) //Splash Screen delay
            keepSplashScreen = false // Hide splash screen after 4 seconds
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
                // Load stored name
                val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                var userName by remember { mutableStateOf(sharedPref.getString("user_name", null)) }

                if (userName == null) {
                    // Show input screen if no name is stored
                    UserInputScreen(onSubmit = { name ->
                        userName = name // Update state to trigger navigation
                    }, context = this@MainActivity)
                } else {
                    // Navigate directly to main app if name is already stored
                    NavigationApp(quizViewModel = quizViewModel, hazardViewModel = hazardViewModel)
                }
            }
        }
    }
}
