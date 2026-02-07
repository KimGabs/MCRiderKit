package com.example.mcriderkit

// Import the views you created
import SignupView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mcriderkit.ui.theme.MCRiderKitTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    val navController = rememberNavController()
    // Determine the start destination based on whether a user is already logged in
    val startDestination = if (com.google.firebase.Firebase.auth.currentUser != null) {
        "home"
    } else {
        "login"
    }

    NavHost(navController, startDestination = startDestination) {
        composable("login") { LoginView(navController) }
        composable("signup") { SignupView(navController) }
        composable("home") { HomeView(navController) }
    }
}