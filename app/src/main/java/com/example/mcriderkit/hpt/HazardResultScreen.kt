package com.example.mcriderkit.hpt

import androidx.annotation.OptIn
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HazardResultScreen(
    score: Int,
    clipId: String,
    userTapTime: Long,
    isDaily: Boolean,
    navController: NavHostController
) {
    var scoreDistribution by remember { mutableStateOf(mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)) }
    val auth = Firebase.auth
    val userId = auth.currentUser?.uid
    var playedCount by remember { mutableIntStateOf(0) }
    var winPercentage by remember { mutableIntStateOf(0) }
    var currentStreak by remember { mutableIntStateOf(0) }
    var maxStreak by remember { mutableIntStateOf(0) }
    var globalDistribution by remember { mutableStateOf(mapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)) }

    LaunchedEffect(clipId) {
        val dateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val communityRef = Firebase.database.getReference("globalStats/$dateKey/$clipId")

        // Fetch global stats for this specific clip
        communityRef.get().addOnSuccessListener { snapshot ->
            val counts = mutableMapOf<Int, Int>()
            for (i in 1..5) {
                counts[i] = snapshot.child(i.toString()).getValue(Int::class.java) ?: 0
            }
            globalDistribution = counts
        }
    }

    LaunchedEffect(userId) {
        if (userId != null) {
            Firebase.database.getReference("users/$userId/hptHistory")
                .get().addOnSuccessListener { snapshot ->
                    val counts = mutableMapOf(1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0)

                    snapshot.children.forEach { child ->
                        // 1. Check if the record is a Daily Challenge
                        // Use the exact key name you used in the save logic (isDaily or Daily)
                        val isDailyRecord = child.child("Daily").getValue(Boolean::class.java) ?: false
                        if (isDailyRecord) {
                            val s = child.child("score").getValue(Int::class.java) ?: 0
                            if (s in 1..5) {
                                counts[s] = (counts[s] ?: 0) + 1
                            }
                        }
                    }
                    scoreDistribution = counts
                }
        }
    }

    LaunchedEffect(userId) {
        if (userId != null) {
            val userRef = Firebase.database.getReference("users/$userId")

            userRef.get().addOnSuccessListener { snapshot ->
                // 1. Get Streak Data (These are already Daily-only based on our save logic)
                currentStreak = snapshot.child("streakCount").getValue(Int::class.java) ?: 0
                maxStreak = snapshot.child("maxStreak").getValue(Int::class.java) ?: 0

                // 2. Filter History to ONLY include Daily Challenges
                val dailyHistory = snapshot.child("hptHistory").children.mapNotNull { child ->
                    // Check the flag (ensure this matches your key: "isDaily" or "Daily")
                    val isDaily = child.child("Daily").getValue(Boolean::class.java) ?: false

                    if (isDaily) {
                        child.child("score").getValue(Int::class.java)
                    } else {
                        null // Ignore practice tests
                    }
                }

                // 3. Calculate Stats based ONLY on the filtered list
                playedCount = dailyHistory.size
                if (playedCount > 0) {
                    // Win = Score of 3 or higher
                    val wins = dailyHistory.count { it >= 1 }
                    winPercentage = (wins.toFloat() / playedCount * 100).toInt()
                } else {
                    winPercentage = 0
                }
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. Daily Header
        if (isDaily) {
            Text(
                text = "DAILY CHALLENGE COMPLETED",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFFF9800),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = "Hazard Perception Result",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 2. The 5-Star Rating Display
        Row {
            repeat(5) { index ->
                Icon(
                    imageVector = if (index < score) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = if (index < score) Color(0xFFFFB300) else Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Performance Feedback
        Text(
            text = when (score) {
                5 -> "Perfect Reaction! (5/5)"
                4 -> "Great Timing! (4/5)"
                3 -> "Good Awareness (3/5)"
                2 -> "A Bit Slow (2/5)"
                1 -> "Very Late Reaction (1/5)"
                else -> "Hazard Missed (0/5)"
            },
            style = MaterialTheme.typography.titleLarge,
            color = if (score > 0) Color(0xFF2E7D32) else Color.Red
        )

        // Streak Feedback
        if (isDaily) {
            var selectedDistributionTab by remember { mutableIntStateOf(0) }

            WordleStatsBar(playedCount, winPercentage, currentStreak, maxStreak)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                shape = RoundedCornerShape(16.dp), // Smooth rounded corners
                color = Color.White,                // Background of the container
                shadowElevation = 4.dp,             // 🚀 THE "SHADOWY" PART
                tonalElevation = 2.dp               // Adds a subtle Material 3 tint
            ){
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text("SCORE DISTRIBUTION", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge, modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp))

                    // 2. The Tab Switcher
                    TabRow(
                        selectedTabIndex = selectedDistributionTab,
                        containerColor = Color.Transparent,
                        divider = {} // Clean look
                    ) {
                        Tab(
                            selected = selectedDistributionTab == 0,
                            onClick = { selectedDistributionTab = 0 },
                            text = { Text("Personal", style = MaterialTheme.typography.labelLarge) }
                        )
                        Tab(
                            selected = selectedDistributionTab == 1,
                            onClick = { selectedDistributionTab = 1 },
                            text = { Text("Community", style = MaterialTheme.typography.labelLarge) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3. Conditional Rendering based on Tab
                    when (selectedDistributionTab) {
                        0 -> {
                            ScoreDistributionChart(distribution = scoreDistribution, currentScore = score)
                        }
                        1 -> {
                            // Community chart (currentScore is 0 so no bar is highlighted green)
                            ScoreDistributionChart(distribution = globalDistribution, currentScore = score)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isDaily) {
            // 4. Action Buttons
            Button(
                onClick = { navController.navigate("hazard_review/$clipId/$userTapTime") },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Review Test")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Back to Home")
            }

            Text(
                text = "Daily Challenge can only be taken once per day.",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        else {
            Button(
                onClick = { navController.navigate("hazard_review/$clipId/$userTapTime") },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Review Test")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { navController.popBackStack() }, // Goes back to player for 'Try Again'
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(50.dp)
            ) {
                Text("Try Again")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack("hazard_menu", inclusive = false) },
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp).height(50.dp)
            ) {
                Text("Back to Menu")
            }
        }
    }
}

@Composable
fun ScoreDistributionChart(distribution: Map<Int, Int>, currentScore: Int) {
    val maxCount = distribution.values.maxOrNull()?.coerceAtLeast(1) ?: 1

    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        for (score in 5 downTo 1) { // LTO/HPT usually cares about high scores first
            val count = distribution[score] ?: 0
            val progress = count.toFloat() / maxCount
            val isCurrent = score == currentScore

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(text = "$score", modifier = Modifier.width(16.dp), fontWeight = FontWeight.Bold)

                // The "Wordle Bar"
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceAtLeast(0.1f)) // Minimum width so '0' is visible
                        .height(24.dp)
                        .background(if (isCurrent) Color(0xFF2E7D32) else Color(0xFF787C7E))
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Text("$count", color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
fun WordleStatsBar(played: Int, winPct: Int, current: Int, max: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(label = "Played", value = played)
        StatItem(label = "Win %", value = winPct)
        StatItem(label = "Current\nStreak", value = current)
        StatItem(label = "Max\nStreak", value = max)
    }
}

@Composable
fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "$value",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}

@Composable
fun GlobalScoreChart(distribution: Map<Int, Int>) {
    Text(
        text = "COMMUNITY DISTRIBUTION",
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp)
    )

    // Pass 0 as currentScore so no bar is highlighted in green
    ScoreDistributionChart(distribution = distribution, currentScore = 0)
}