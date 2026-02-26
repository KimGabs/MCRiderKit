package com.example.mcriderkit

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.mcriderkit.data.DashboardStats
import com.example.mcriderkit.data.HptResult
import com.example.mcriderkit.data.getMidnightTimestamp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val auth = Firebase.auth
    val db = Firebase.firestore
    val userId = auth.currentUser?.uid
    var username by remember { mutableStateOf("") }
    var stats by remember { mutableStateOf(DashboardStats()) }
    var masteredCount by remember { mutableIntStateOf(0) }
    var totalQuestionsCount by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    var lastHazardDate by remember { mutableLongStateOf(0L) }
    var streakCount by remember { mutableIntStateOf(0) }
    var trophyCount by remember { mutableIntStateOf(0) }

    var dailyClipId by remember { mutableStateOf("") }
    val todayDateKey = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    var lastHazardDateKey by remember { mutableStateOf("") }
    var isProfileLoading by remember { mutableStateOf(true) }
    var todayResult by remember { mutableStateOf<HptResult?>(null) }
    val todayMs = remember { getMidnightTimestamp() }

    var winPercentage by remember { mutableIntStateOf(0) }
    var playedCount by remember { mutableIntStateOf(0) }


    LaunchedEffect(userId) {
        if (userId == null) {
            isProfileLoading = false
            isLoading = false
            return@LaunchedEffect
        }

        // 1. Get Daily Clip ID from Firestore (one-time fetch)
        db.collection("app_metadata").document("daily_challenge").get()
            .addOnSuccessListener { snapshot ->
                dailyClipId = snapshot.getString("currentClipId") ?: ""
            }

        // 2. Fetch total question count (one-time fetch)
        db.collection("questions").get().addOnSuccessListener { result ->
            totalQuestionsCount = result.size()
            isLoading = false // Set loading to false after this is done
        }.addOnFailureListener {
            isLoading = false
        }

        // 3. Attach a single, continuous listener for all user-specific data
        val userRef = Firebase.database.getReference("users/$userId")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // --- Profile & Core Stats ---
                username = snapshot.child("username").getValue(String::class.java) ?: "User"
                val license = snapshot.child("licenseType").getValue(String::class.java) ?: "Non-Pro"
                masteredCount = snapshot.child("masteredQuestions").childrenCount.toInt()
                streakCount = snapshot.child("streakCount").getValue(Int::class.java) ?: 0
                lastHazardDate = snapshot.child("lastHazardDate").getValue(Long::class.java) ?: 0L
                trophyCount = snapshot.child("trophyCount").getValue(Int::class.java) ?: 0

                stats = DashboardStats(masteredCount, totalQuestionsCount, username, license)

                // --- HPT History Analysis (Filter for Daily Challenges) ---
                val dailyHistory = snapshot.child("hptHistory").children.mapNotNull {
                    val result = it.getValue(HptResult::class.java)
                    if (result?.isDaily == true) result else null // Only include daily challenges
                }

                playedCount = dailyHistory.size
                winPercentage = if (playedCount > 0) {
                    val wins = dailyHistory.count { it.score >= 1 } // A win is a score of 1 or more
                    (wins.toFloat() / playedCount * 100).toInt()
                } else 0

                // --- Check if Today's Daily Challenge is Completed ---
                if (lastHazardDate >= todayMs) {
                    // If the last completion date is today, find the specific result
                    todayResult = dailyHistory.find { it.dateKey == todayDateKey }
                    lastHazardDateKey = if (todayResult != null) todayDateKey else ""
                } else {
                    // If last completion was before today, it's not done
                    todayResult = null
                    lastHazardDateKey = ""
                }

                scope.launch {
                    delay(200) // Small delay to prevent UI flicker on initial load
                    isProfileLoading = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isProfileLoading = false
                isLoading = false
                Log.e("FirebaseListener", "Database error: ${error.message}")
            }
        }

        userRef.addValueEventListener(listener)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        HeaderSection(username = stats.username)

        Column(
            modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))
                .verticalScroll(rememberScrollState())
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                ReadinessCard(mastered = masteredCount, total = totalQuestionsCount, isLoading)

                StatsSection(streakCount = streakCount, winPercentage = winPercentage, isProfileLoading, trophyCount)

                LearningSection(
                    navController = navController,
                    lastHazardDateKey = lastHazardDateKey,
                    dailyClipId = dailyClipId,
                    todayDateKey = todayDateKey,
                    todayResult = todayResult,
                    isProfileLoading = isProfileLoading,
                    context = context
                )

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun LearningSection(
    navController: NavHostController,
    lastHazardDateKey: String,
    dailyClipId: String,
    todayDateKey: String,
    todayResult: HptResult?,
    isProfileLoading: Boolean,
    context: Context
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (isProfileLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().height(100.dp).padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
            }
        } else {
            DailyChallengeCard(
                lastHazardDateKey = lastHazardDateKey,
                todayDateKey = todayDateKey,
                todayResult = todayResult,
                onStart = {
                    if (dailyClipId.isNotEmpty()) {
                        navController.navigate("hazard_player/$dailyClipId/true")
                    } else {
                        // Optional: Add a fallback or show an error if the daily clip ID isn't loaded
                    }
                },
                onViewResults = {
                    todayResult?.let {
                        navController.navigate("hazard_result/${it.score}/${it.clipId}/${it.tapTime}/true")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Start Learning", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(12.dp))

        LearningCard(
            title = "Study Materials",
            subtitle = "Learn traffic rules & signs",
            color = Color(0xFFE8F0FF),
            onClick = { navController.navigate("study") {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            } }
        )

        LearningCard(
            title = "Take Mock Exam",
            subtitle = "Test your knowledge",
            color = Color(0xFFE5F8F1),
            onClick = { navController.navigate("quiz") {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            } }
        )

        LearningCard(
            title = "Hazard Perception Test",
            subtitle = "Improve your awareness",
            color = Color(0xFFFFF1DB),
            onClick = { navController.navigate("hazard") {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            } }
        )
    }
}

@Composable
fun HeaderSection(username: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(Brush.verticalGradient(colors = listOf(Color(0xFF2A6CF6), Color(0xFF1A4FD9))))
            .padding(24.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Welcome Back, $username!", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "MCRider's Kit", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StatsSection(streakCount: Int, winPercentage: Int, isProfileLoading: Boolean, trophyCount: Int) {
    if (isProfileLoading) {
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp).padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("$streakCount", "Daily Streak", Color(0xFFDDE9FF))
            StatCard("$winPercentage%", "Win %", Color(0xFFDCF6E5))
            StatCard("$trophyCount", "Awards", Color(0xFFFFF0CC))
        }
    }
}

@Composable
fun StatCard(value: String, label: String, bgColor: Color) {
    Card(
        modifier = Modifier.width(110.dp).height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().background(bgColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val emoji = when (label) {
                "Daily Streak" -> if (value.toIntOrNull() ?: 0 > 0) "🔥" else "❄️"
                "Win %" -> when (value.removeSuffix("%").toIntOrNull() ?: 0) {
                    100 -> "🤯"
                    in 90..99 -> "🤩"
                    in 75..89 -> "😎"
                    in 50..74 -> "🙂"
                    in 25..49 -> "😐"
                    else -> "🫠"
                }
                "Awards" -> "🏆"
                else -> ""
            }
            Text(emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun LearningCard(title: String, subtitle: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).height(80.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().background(color).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = subtitle, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ReadinessCard(mastered: Int, total: Int, isLoading: Boolean) {
    val progress = if (total > 0) mastered.toFloat() / total else 0f
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Exam Readiness", fontWeight = FontWeight.Bold)
                    Text("${(progress * 100).toInt()}%", color = Color(0xFF2A6CF6), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF2A6CF6),
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = StrokeCap.Round
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("You have mastered $mastered out of $total questions.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}

@Composable
fun DailyChallengeCard(
    lastHazardDateKey: String,
    todayDateKey: String,
    todayResult: HptResult?,
    onStart: () -> Unit,
    onViewResults: () -> Unit
) {
    val isCompleted = lastHazardDateKey == todayDateKey

    Card(
        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().height(80.dp).clickable { if (isCompleted) onViewResults() else onStart() },
        colors = CardDefaults.cardColors(containerColor = if (isCompleted) Color(0xFFE8F5E9) else Color(0xFFFFF4E5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(if (isCompleted) "✅" else "🎯", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isCompleted) "Daily Challenge Done!" else "Daily Hazard Challenge",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = if (isCompleted) "You scored ${todayResult?.score ?: 0}/5 today."
                    else "Test your eyes to keep your streak!",
                    fontSize = 13.sp
                )
            }
            Icon(imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight, contentDescription = null)
        }
    }
}

