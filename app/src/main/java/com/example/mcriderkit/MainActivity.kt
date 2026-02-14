package com.example.mcriderkit

// Import the views you created
import SignupScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mcriderkit.ui.theme.MCRiderKitTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()

        // Correct initialization of Firebase
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            MCRiderKitTheme {
                // You need to call your main app composable here
                AuthApp()
            }
        }
    }
}


@Composable
fun AuthApp(){
    val rootNavController = rememberNavController()
    // Determine the start destination based on whether a user is already logged in
    val startDestination = if (Firebase.auth.currentUser != null) {
        "main"
    } else {
        "login"
    }

    NavHost(rootNavController, startDestination = startDestination) {
        composable("login") { LoginScreen(rootNavController) }
        composable("signup") { SignupScreen(rootNavController) }
        composable("onboard") { OnboardScreen(rootNavController) }
        composable("main") { MainScreen(rootNavController = rootNavController) }
    }
}