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
import androidx.compose.material3.Button
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
import com.example.mcriderkit.data.Question
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
import java.util.Calendar
import java.util.Date
import java.util.Locale


@OptIn(UnstableApi::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    // Get the current user from Firebase Auth
    val context = LocalContext.current
    val auth = Firebase.auth
    val db = Firebase.firestore
    val userId = auth.currentUser?.uid
    var username by remember { mutableStateOf("") }
    var stats by remember { mutableStateOf(DashboardStats()) }
    var masteredCount by remember { mutableIntStateOf(0) }
    var totalQuestionsCount by remember { mutableIntStateOf(0) }
    var questions by remember { mutableStateOf(listOf<Question>()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    var lastHazardDate by remember { mutableLongStateOf(0L) }
    var streakCount by remember { mutableIntStateOf(0) }
    var trophyCount by remember { mutableIntStateOf(0) }

    var dailyClipName by remember { mutableStateOf("") }
    val todayDateKey = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }
    var lastHazardDateKey by remember { mutableStateOf("") }
    var isProfileLoading by remember { mutableStateOf(true) } // Start as true
    var todayResult by remember { mutableStateOf<HptResult?>(null) }
    val todayMs = remember { getMidnightTimestamp() }

    var winPercentage by remember { mutableIntStateOf(0) }
    var currentStreak by remember { mutableIntStateOf(0) }
    var maxStreak by remember { mutableIntStateOf(0) }
    var playedCount by remember { mutableIntStateOf(0) }
    var currentTrophyCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("app_metadata").document("daily_challenge")
            .get()
            .addOnSuccessListener { snapshot ->
                dailyClipName = snapshot.getString("currentClipId") ?: ""
            }
    }

    LaunchedEffect(userId) {
        if (userId != null) {
            val userRef = Firebase.database.getReference("users/$userId")

            userRef.get().addOnSuccessListener { snapshot ->
                // 1. Get Streak Data (These are already Daily-only based on our save logic)
                currentStreak = snapshot.child("streakCount").getValue(Int::class.java) ?: 0
                maxStreak = snapshot.child("maxStreak").getValue(Int::class.java) ?: 0
                trophyCount = snapshot.child("trophyCount").getValue(Int::class.java) ?: 0


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

    LaunchedEffect(userId) {
        if (userId != null) {
            val dbRef = Firebase.database.getReference("users/$userId/username")
            dbRef.get().addOnSuccessListener { snapshot ->
                val name = snapshot.getValue(String::class.java)
                if (name != null) {
                    username = name
                }
            }

            db.collection("questions").get().addOnSuccessListener { result ->
                val fetchedQuestions = result.toObjects(Question::class.java)
                questions = fetchedQuestions
                totalQuestionsCount = fetchedQuestions.size // Set total here
                isLoading = false

                // 2. Now that we have the total, listen for Mastery updates
                val userRef = Firebase.database.getReference("users/$userId")
                userRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("username").getValue(String::class.java) ?: "User"
                        val license = snapshot.child("licenseType").getValue(String::class.java) ?: "Non-Pro"
                        val mastered = snapshot.child("masteredQuestions").childrenCount.toInt()

                        // Fetch the streak count for the header
                        streakCount = snapshot.child("streakCount").getValue(Int::class.java) ?: 0
                        //Fetch the last hazard date for the Daily Challenge Card
                        lastHazardDate = snapshot.child("lastHazardDate").getValue(Long::class.java) ?: 0L

                        // Update everything together
                        username = name
                        masteredCount = mastered
                        stats = DashboardStats(mastered, totalQuestionsCount, name, license)
                        scope.launch {
                            delay(200)
                            isProfileLoading = false
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        isProfileLoading = false
                    }
                })
            }.addOnFailureListener {
                isLoading = false
            }
        }
    }

    LaunchedEffect(userId, lastHazardDate) {
        if (userId != null && lastHazardDate == todayMs) {
            val historyRef = Firebase.database.getReference("users/$userId/hptHistory")

            // 🚀 Step 1: Get only the results marked as "Daily"
            // We take the last 5 to be safe, then find the one that matches today's date
            historyRef.orderByChild("Daily").equalTo(true).limitToLast(5)
                .get().addOnSuccessListener { snapshot ->
                    val results = snapshot.children.mapNotNull { it.getValue(HptResult::class.java) }

                    // Find a result that happened ANYTIME after today's midnight
                    val oneDayMs = 24 * 60 * 60 * 1000L
                    val result = results.find { it.date >= todayMs && it.date < (todayMs + oneDayMs) }

                    todayResult = result
                }
        }
    }

    LaunchedEffect(userId, lastHazardDate) {
        if (userId != null) {
            val historyRef = Firebase.database.getReference("users/$userId/hptHistory")
            // 🚀 Search specifically for today's DateKey
            historyRef.orderByChild("dateKey").equalTo(todayDateKey)
                .get().addOnSuccessListener { snapshot ->
                    if (snapshot.exists()) {
                        val result = snapshot.children.first().getValue(HptResult::class.java)
                        todayResult = result
                        lastHazardDateKey = todayDateKey // Mark as completed
                        val hazCompleted = lastHazardDate == todayMs
                        Log.d("DEBUG", "Today's result found: $todayMs")
                        Log.d("DEBUG", "lastHazardDate found: $lastHazardDate")
                        Log.d("DEBUG", "Is Completed?: $hazCompleted")
                    }
                }
        }
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
                // Show a shimmer or loader while fetching data
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                ReadinessCard(mastered = masteredCount, total = totalQuestionsCount, isLoading)

                StatsSection(streakCount = streakCount, winPercentage = winPercentage, isProfileLoading, trophyCount)

                LearningSection(navController = navController, lastHazardDateKey = lastHazardDateKey, dailyClipName = dailyClipName, todayDateKey = todayDateKey,
                    todayResult = todayResult, isProfileLoading, context = context)

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

}
@SuppressLint("DiscouragedApi")
@Composable
fun LearningSection(navController: NavHostController, lastHazardDateKey: String, dailyClipName: String, todayDateKey: String, todayResult: HptResult?, isProfileLoading : Boolean, context: Context) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        if (isProfileLoading) {
            // Show a loading placeholder so the screen doesn't "jump"
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp),
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
                    // START LOGIC GOES HERE
                    if (dailyClipName.isNotEmpty()) {
                        val resId = context.resources.getIdentifier(
                            dailyClipName, "raw", context.packageName
                        )
                        navController.navigate("hazard_player/$resId/true")
                    } else {
                        val dailyClip = getDailyClipResId()
                        navController.navigate("hazard_player/$dailyClip/true")
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

        Text(
            text = "Start Learning",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningCard(
            title = "Study Materials",
            subtitle = "Learn traffic rules & signs",
            color = Color(0xFFE8F0FF),
            onClick = { navController.navigate("study")
                {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        LearningCard(
            title = "Take Mock Exam",
            subtitle = "Test your knowledge",
            color = Color(0xFFE5F8F1),
            onClick = { navController.navigate("quiz"){
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

        LearningCard(
            title = "Hazard Perception Test",
            subtitle = "Improve your awareness",
            color = Color(0xFFFFF1DB),
            onClick = { navController.navigate("hazard")                {
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

@Composable
fun HeaderSection(username: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A6CF6),
                        Color(0xFF1A4FD9)
                    )
                )
            )
            .padding(24.dp)
    ) {

        Column {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome Back, $username!",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "MCRider's Kit",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatsSection(streakCount: Int, winPercentage: Int, isProfileLoading: Boolean, trophyCount: Int) {
    if(isProfileLoading){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(24.dp))
        }
    }
    else{
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            StatCard("$streakCount", "Daily Streak", Color(0xFFDDE9FF), streakCount, winPercentage)
            StatCard("$winPercentage%", "Win %", Color(0xFFDCF6E5), streakCount, winPercentage)
            StatCard("$trophyCount", "Awards", Color(0xFFFFF0CC), streakCount, winPercentage)
        }
    }
}


@Composable
fun StatCard(
    value: String,
    label: String,
    bgColor: Color,
    streakCount: Int,
    winPercentage: Int
) {

    Card(
        modifier = Modifier
            .width(110.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(label == "Daily Streak"){
                if (streakCount > 0) { Text("🔥", fontSize = 24.sp) } else { Text("❄️", fontSize = 24.sp) }
            }
            if(label == "Win %"){
                when(winPercentage){
                    100 -> Text("🤯", fontSize = 24.sp)
                    in 90..99 -> Text("🤩", fontSize = 24.sp)
                    in 75..89 -> Text("😎", fontSize = 24.sp)
                    in 50..74 -> Text("🙂", fontSize = 24.sp)
                    in 25..49 -> Text("😐", fontSize = 24.sp)
                    else -> Text("🫠", fontSize = 24.sp)
                }
            }
            if(label == "Awards"){
                Text("🏆", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun LearningCard(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
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
        if(isLoading){
            Box(modifier = Modifier.fillMaxSize().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else{
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

                Text(
                    "You have mastered $mastered out of $total questions.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
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
        modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth().height(80.dp),
        onClick = { if (isCompleted) onViewResults() else onStart()},
        enabled = true,
        colors = CardDefaults.cardColors(
            containerColor = if (isCompleted) Color(0xFFE8F5E9) else Color(0xFFFFF4E5)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Added the checkmark back for the completed state
            Text(if (isCompleted) "✅ " else "🎯", style = MaterialTheme.typography.headlineSmall)

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

            if (!isCompleted) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                )
            }
        }
    }
}

// Daily challenge video randomly seeded (will be same video for all users).
fun getDailyClipResId(): Int {
    val calendar = Calendar.getInstance()
    val dayOfYear = calendar.get(Calendar.DAY_OF_YEAR)
    val year = calendar.get(Calendar.YEAR)

    // Create a unique number for the day
    val seed = dayOfYear + year

    // Use 6 video resources
    val clips = listOf(R.raw.hpt1, R.raw.hpt2, R.raw.hpt3, R.raw.hpt4, R.raw.hpt5, R.raw.hpt6)

    // Use the modulo operator (%) to pick an index (0, 1, 2, or 3)
    val index = seed % clips.size

    return clips[index]
}

@Composable
fun PushToFirebaseButton(){
    val context = LocalContext.current

    // Json to Realtime Database Button
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Database Management")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            // 2. Call the utility function
            // pushQueJsonToFirestore(context)
            migrateLicenseTypes()
        }) {
            Text("Import JSON HPTs")
        }
    }
}

@OptIn(UnstableApi::class)
fun migrateLicenseTypes() {
    val db = Firebase.firestore
    db.collection("questions").get().addOnSuccessListener { snapshot ->
        val batch = db.batch()
        var updatedCount = 0

        for (doc in snapshot.documents) {
            val category = doc.getString("category") ?: ""

            // 🚀 YOUR LOGIC: Check for the two Pro categories
            val type = if (category == "Passenger & Public Safety" ||
                category == "Commercial Vehicle Operation") "Pro" else "Non-Pro"

            // Only update if the field is missing or different to save on writes
            if (doc.getString("licenseType") != type) {
                batch.update(doc.reference, "licenseType", type)
                updatedCount++
            }

            // Firestore batch limit is 500
            if (updatedCount >= 490) break
        }

        batch.commit().addOnSuccessListener {
            Log.d("MIGRATION", "Successfully tagged $updatedCount questions!")
        }
    }
}