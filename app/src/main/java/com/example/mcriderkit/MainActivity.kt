package com.example.mcriderkit

import android.R.attr.content
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.mcriderkit.data.QuizDatabase
import com.example.mcriderkit.ui.HazardTestViewModel
import com.example.mcriderkit.ui.NonProExamViewModel
import com.example.mcriderkit.ui.ProExamViewModel
import com.example.mcriderkit.ui.StudentExamViewModel
import com.example.mcriderkit.ui.UserInputScreen
import com.example.mcriderkit.ui.components.HazardRepository
import com.example.mcriderkit.ui.components.QuizRepository
import com.example.mcriderkit.ui.local.LocalAppContext
import com.example.mcriderkit.ui.theme.MCRiderKitTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable
import java.util.Locale


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()
        var keepSplashScreen = true
        val isDarkMode = PreferenceHelper.isDarkMode(this)
        var currentLanguage = mutableStateOf("en")

        splashScreen.setKeepOnScreenCondition { keepSplashScreen }

        lifecycleScope.launch() {
            delay(3000) //Splash Screen delay
            keepSplashScreen = false // Hide splash screen after 4 seconds
        }
        // Initialize database and repository
        val database = QuizDatabase.getDatabase(applicationContext)
        val quizRepository = QuizRepository(database.quizScoreDao())
        val hazardRepository = HazardRepository(database.hazardTestDao())

        val nonProQuizViewModel = NonProExamViewModel(quizRepository)
        val proQuizViewModel = ProExamViewModel(quizRepository)
        val studentExamViewModel = StudentExamViewModel(quizRepository)
        val hazardViewModel = HazardTestViewModel(hazardRepository)

        enableEdgeToEdge()

            setContent {

                var darkMode by remember { mutableStateOf(isDarkMode) }
                val updatedContext = remember(currentLanguage.value) {
                    LocaleUtils.updateLocale(this, currentLanguage.value)
                }

                CompositionLocalProvider(LocalAppContext provides updatedContext){
                    MCRiderKitTheme(
                        darkTheme = darkMode
                    ) {
                        // Load stored name
                        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        var userName by remember { mutableStateOf(sharedPref.getString("user_name", null)) }

                        if (userName == null) {
                            // Show input screen if no name is stored
                            UserInputScreen(onSubmit = { name ->
                                userName = name // Update state to trigger navigation
                            }, context = this@MainActivity)
                        } else {
                            // Navigate directly to main app if name is already stored
                            NavigationApp(
                                nonProQuizViewModel = nonProQuizViewModel,
                                proQuizViewModel = proQuizViewModel,
                                studentExamViewModel = studentExamViewModel,
                                hazardViewModel = hazardViewModel,
                                darkMode = darkMode,
                                onToggleDarkMode = {
                                    darkMode = it
                                    PreferenceHelper.setDarkMode(this@MainActivity, it)
                                }
                            )
                        }
                    }
                }
            }
    }

    private fun getSavedLanguage(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", null)
    }
}

object PreferenceHelper {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_DARK_MODE = "dark_mode"

    fun setDarkMode(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply()
    }

    fun isDarkMode(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_DARK_MODE, false)
    }
}


object LocaleUtils {
    fun updateLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}