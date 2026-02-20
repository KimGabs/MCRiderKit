package com.example.mcriderkit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mcriderkit.exam.MockExamScreen
import com.example.mcriderkit.exam.QuizMenuScreen
import com.example.mcriderkit.hpt.HazardDashboard
import com.example.mcriderkit.hpt.HazardMenuScreen
import com.example.mcriderkit.hpt.HazardPlayerScreen
import com.example.mcriderkit.hpt.HazardResultScreen
import com.example.mcriderkit.hpt.HazardReviewScreen
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


// Global or ViewModel state
var showTrophyBanner by mutableStateOf(false)
var lastEarnedTrophyName by mutableStateOf("")

@SuppressLint("DiscouragedApi")
@Composable
fun MainScreen(rootNavController: NavHostController) {
    val navController = rememberNavController()
    var selectedItem by rememberSaveable { mutableStateOf(0) }

    // Auto-hide the banner after 5 seconds
    LaunchedEffect(showTrophyBanner) {
        if (showTrophyBanner) {
            delay(5000)
            showTrophyBanner = false
        }
    }

    val state = remember { Party(
        speed = 0f,
        maxSpeed = 30f,
        damping = 0.9f,
        spread = 360,
        colors = listOf(
            0xFFFFD700.toInt(), // Gold
            0xFFFFFFFF.toInt(), // White
            0xFF1A4FD9.toInt()  // Blue
        ), // Gold, White, Blue
        position = Position.Relative(0.5, 0.3),
        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
    ) }



    val items = listOf("Home", "Study", "Quiz", "Hazard", "Profile")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Info,
        Icons.Filled.Edit,
        Icons.Filled.Warning,
        Icons.Filled.Person
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Only show BottomBar if we are NOT on the quiz screen
    val shouldShowBottomBar = currentRoute != null &&
            !currentRoute.startsWith("quiz/") &&
            !currentRoute.startsWith("hazard_player") &&
            !currentRoute.startsWith("hazard_review")

    // Update selectedItem based on the current route
    selectedItem = when (currentRoute) {
        "study" -> 1
        "quiz" -> 2
        "hazard" -> 3
        "hazard_menu" -> 3
        "hazard_result" -> 3
        "profile" -> 4
        else -> 0
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Scaffold(
            bottomBar = {
                if (shouldShowBottomBar) {
                    NavigationBar {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = { Icon(icons[index], contentDescription = item) },
                                label = { Text(item) },
                                selected = selectedItem == index,
                                onClick = {
                                    // This navigates to "home", "study", "quiz", etc.
                                    navController.navigate(item.lowercase()) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController,
                startDestination = "home",
                modifier = Modifier.padding(padding)
            ) {
                composable("home") { HomeScreen(navController) }
                composable("study") { StudyScreen(navController) }
                composable("quiz") { QuizMenuScreen(navController) }

                composable(
                    route = "quiz/{category}",
                    arguments = listOf(navArgument("category") { type = NavType.StringType })
                ) { backStackEntry ->
                    val category = backStackEntry.arguments?.getString("category") ?: "All"
                    MockExamScreen(navController, category)
                }

                composable("hazard") { HazardDashboard(navController) }
                composable("hazard_menu") { HazardMenuScreen(navController) }

                composable(
                    route = "hazard_player/{resId}/{isDaily}",
                    arguments = listOf(
                        navArgument("resId") { type = NavType.IntType },
                        navArgument("isDaily") { type = NavType.BoolType }
                    )
                ) { backStackEntry ->
                    val resId = backStackEntry.arguments?.getInt("resId") ?: 0
                    val isDaily = backStackEntry.arguments?.getBoolean("isDaily") ?: false
                    HazardPlayerScreen(resId = resId, isDaily, navController = navController)
                }

                composable(
                    route = "hazard_result/{score}/{clipId}/{userTapTime}/{isDaily}",
                    arguments = listOf(
                        navArgument("score") { type = NavType.IntType },
                        navArgument("clipId") { type = NavType.StringType },
                        navArgument("userTapTime") { type = NavType.LongType },
                        navArgument("isDaily") { type = NavType.BoolType }
                    )
                ) { backStackEntry ->
                    val score = backStackEntry.arguments?.getInt("score") ?: 0
                    val clipId = backStackEntry.arguments?.getString("clipId") ?: ""
                    val userTapTime = backStackEntry.arguments?.getLong("userTapTime") ?: 0L
                    val isDaily = backStackEntry.arguments?.getBoolean("isDaily") ?: false
                    HazardResultScreen(score, clipId, userTapTime, isDaily, navController)
                }

                composable(
                    route = "hazard_review/{clipId}/{userTapTime}",
                    arguments = listOf(
                        navArgument("clipId") { type = NavType.StringType },
                        navArgument("userTapTime") { type = NavType.LongType }
                    )
                ) { backStackEntry ->
                    val clipId = backStackEntry.arguments?.getString("clipId") ?: ""
                    val userTapTime = backStackEntry.arguments?.getLong("userTapTime") ?: 0L
                    HazardReviewScreen(clipId, userTapTime, navController)
                }

                composable("profile") { ProfileScreen(navController, rootNavController) }
            }
            // --- THE GLOBAL OVERLAY ---
            // This stays on top of everything, even the BottomBar if you want
            TrophyEarnedBanner(
                visible = showTrophyBanner,
                message = lastEarnedTrophyName,
                onDismiss = { showTrophyBanner = false }
            )
        }
        if (showTrophyBanner) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(state)
            )
        }
    }
}
