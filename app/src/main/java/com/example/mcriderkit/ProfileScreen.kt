package com.example.mcriderkit

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.mcriderkit.data.ExamResult
import com.example.mcriderkit.data.HptResult
import com.example.mcriderkit.data.calculateDaysUntil
import com.example.mcriderkit.data.formatDate
import com.example.mcriderkit.data.pushJsonToFirestore
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(navController: NavHostController, rootNavController: NavController) {
    val userId = Firebase.auth.currentUser?.uid
    var username by remember { mutableStateOf("Loading...") }
    var licenseType by remember { mutableStateOf("") }
    var examDate by remember { mutableLongStateOf(0L) }

    var examHistory by remember { mutableStateOf(listOf<ExamResult>()) }
    var hptHistory by remember { mutableStateOf(listOf<HptResult>()) }
    var hptTitles by remember { mutableStateOf(mapOf<String, String>()) }
    var visibleExamCount by remember { mutableIntStateOf(5) }
    var visibleHptCount by remember { mutableIntStateOf(5) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var showClearDialog by remember { mutableStateOf(false) }
    var clearType by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showLicenseDialog by remember { mutableStateOf(false) }
    val licenseOptions = listOf("Non-Professional", "Professional")
    var maxStreak by remember { mutableIntStateOf(0) }
    var perfectScoreCount by remember { mutableIntStateOf(0) }
    var examPassCount by remember { mutableIntStateOf(0) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${month + 1}/$year"
            if (userId != null) {
                updateExamDate(userId, selectedDate)
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    LaunchedEffect(userId) {
        if (userId != null) {
            Firebase.firestore.collection("hazard_clips").get().addOnSuccessListener { result ->
                val titleMap = result.documents.associate {
                    it.id to (it.getString("title") ?: "Unknown Clip")
                }
                hptTitles = titleMap
            }

            val userRef = Firebase.database.getReference("users/$userId")

            // 1. Fetch Profile & Exam History
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    username = snapshot.child("username").getValue(String::class.java) ?: "User"
                    licenseType = snapshot.child("licenseType").getValue(String::class.java) ?: "N/A"

                    maxStreak = snapshot.child("maxStreak").getValue(Int::class.java) ?: 0
                    val masteredClips = snapshot.child("masteredClips").children.mapNotNull {
                        it.value as? String
                    }
                    examPassCount = snapshot.child("examPassCount").getValue(Int::class.java) ?: 0

                    perfectScoreCount = masteredClips.size

                    val examDateStr = snapshot.child("examDate").getValue(String::class.java) ?: ""
                    if (examDateStr.isNotEmpty()) {
                        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                        try {
                            val parsedDate = sdf.parse(examDateStr)
                            if (parsedDate != null) {
                                examDate = parsedDate.time // Convert string to Long timestamp
                            }
                        } catch (e: Exception) {
                            Log.e("DateError", "Failed to parse: $examDateStr")
                        }
                    }

                    // Map Exam History
                    examHistory = snapshot.child("examHistory").children.mapNotNull {
                        it.getValue(ExamResult::class.java)
                    }.reversed() // Newest first

                    // Map HPT History
                    hptHistory = snapshot.child("hptHistory").children.mapNotNull {
                        it.getValue(HptResult::class.java)
                    }.reversed()
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA))) {
        // --- SECTION 1: USER INFO ---
        ProfileHeader(username, licenseType, examDate = examDate)

        // --- SECTION 2: HISTORY TABS ---
        var selectedTab by remember { mutableIntStateOf(0) }
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                // Standard Material 3 blue underline
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF1A4FD9)
                )
            }, divider = {}
        ) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("Awards", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("Mock Exams", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                Text("Hazard Tests", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }) {
                Text("Settings", modifier = Modifier.padding(16.dp))
            }
        }

        // --- SECTION 3: SCROLLABLE HISTORY ---
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            when(selectedTab) {
                0 -> {
                    item {
                        AwardsTab(maxStreak, perfectScoreCount, examPassCount)
                    }
                }
                1 -> {
                    // --- MOCK EXAMS TAB ---
                    val displayedExams = examHistory.take(visibleExamCount)
                    if (displayedExams.isNotEmpty()){
                        items(displayedExams) { exam -> ExamHistoryCard(exam) }
                    }else{
                        item {
                            emptySpace()
                        }
                    }

                    // "Load More" Button for Exams
                    if (examHistory.size > visibleExamCount) {
                        item {
                            LoadMoreButton { visibleExamCount += 5 }
                        }
                    }
                }
                2 -> {
                    // --- HAZARD TESTS TAB ---
                    val displayedHpts = hptHistory.take(visibleHptCount)
                    if(displayedHpts.isNotEmpty()){
                        items(displayedHpts) { hpt -> HptHistoryCard(hpt, hptTitles) }
                    }else{
                        item {
                            emptySpace()
                        }
                    }

                    // "Load More" Button for Hazards
                    if (hptHistory.size > visibleHptCount) {
                        item {
                            LoadMoreButton { visibleHptCount += 5 }
                        }
                    }
                }
                3 -> {
                    item {
                        SettingsSection(
                            onLogout = {
                                Firebase.auth.signOut()
                                rootNavController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            },
                            onDeleteAccount = { showDeleteDialog = true },
                            onClearExams = {
                                clearType = "Exams"
                                showClearDialog = true
                            },
                            onClearHpt = {
                                clearType = "HPTs"
                                showClearDialog = true
                            },
                            onChangeDate = { datePickerDialog.show() },
                            onChangeLicense = { showLicenseDialog = true }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account?") },
            text = { Text("This will permanently erase your study progress, exam history, and account. This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    deleteUserAccount(rootNavController)
                }) {
                    Text("Delete Permanently", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Clear $clearType History?") },
            text = { Text("Are you sure? This will permanently delete all your $clearType records.") },
            confirmButton = {
                TextButton(onClick = {
                    if (userId != null) {
                        if (clearType == "Exams") clearExamHistory(userId)
                        else clearHptHistory(userId)
                    }
                    showClearDialog = false
                }) { Text("Clear", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) { Text("Cancel") }
            }
        )
    }
    if (showLicenseDialog) {
        AlertDialog(
            onDismissRequest = { showLicenseDialog = false },
            title = { Text("Change Target License") },
            text = {
                Column {
                    licenseOptions.forEach { option ->
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                if (userId != null) updateLicenseType(userId, option)
                                showLicenseDialog = false
                            }
                        ) {
                            Text(option, textAlign = TextAlign.Left, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            },
            confirmButton = {} // No "OK" button needed since clicking an option closes it
        )
    }
}

@Composable
fun ProfileHeader(username: String, licenseType: String, examDate: Long) {
    val daysUntil = calculateDaysUntil(examDate)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A4FD9), Color(0xFF2A6CF6))
                )
            )
            .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: User Info
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = username.take(1).uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Target: $licenseType",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            // Right: Countdown Badge
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (daysUntil > 0) {
                    Text(
                        text = "$daysUntil",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = if (daysUntil == 1L) "Day Left" else "Days Left Until Exam",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }else {
                    Icon(
                        imageVector = Icons.Default.Star, // or Icons.Default.Flag
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "EXAM DAY!",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

            }
        }
    }
}

@Composable
fun LoadMoreButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null)
            Text("Show 5 more results", style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun ExamHistoryCard(exam: ExamResult) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = if (exam.examType == "All") "Full Mock Exam" else exam.examType, fontWeight = FontWeight.Bold)
                Text(text = formatDate(exam.date), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${exam.score}/${exam.total}",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (exam.passed) Color(0xFF2E7D32) else Color.Red
                )
                Text(
                    text = if (exam.passed) "PASSED" else "FAILED",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (exam.passed) Color(0xFF2E7D32) else Color.Red
                )
            }
        }
    }
}

@Composable
fun HptHistoryCard(hpt: HptResult, titleMap: Map<String, String>) {
    val displayTitle = titleMap[hpt.clipId] ?: hpt.clipId

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                if (hpt.isDaily){
                    Text(text = "Daily: $displayTitle" , fontWeight = FontWeight.Bold)
                }else{
                    Text(text = displayTitle , fontWeight = FontWeight.Bold)
                }
                Text(text = formatDate(hpt.date), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(text = "${hpt.score}/5", style = MaterialTheme.typography.titleMedium)
        }
    }
}

fun deleteUserAccount(rootNavController: NavController) {
    val user = Firebase.auth.currentUser
    val userId = user?.uid

    if (userId != null && user != null) {
        // 1. Delete Database Data First
        Firebase.database.getReference("users/$userId").removeValue()
            .addOnSuccessListener {
                // 2. Delete Auth Account
                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 3. Success: Navigate back to Login
                        rootNavController.navigate("login") {
                            // Completely wipe the "main" app state
                            popUpTo(0) { inclusive = true }
                        }
                    } else {
                        // 4. Handle "Recent Login Required" error
                        Toast.makeText(
                            rootNavController.context,
                            "Please re-login to verify your identity before deleting.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(rootNavController.context, "Failed to remove data: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

@Composable
fun SettingsSection(onLogout: () -> Unit, onDeleteAccount: () -> Unit, onClearExams: () -> Unit, onClearHpt: () -> Unit, onChangeDate: () -> Unit, onChangeLicense: () -> Unit,) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
        Text("Preference", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        // Change Date of Exam
        SettingsItem(label = "Change Exam Date", icon = Icons.Filled.DateRange, onClick = onChangeDate)

        SettingsItem(label = "Change License Type", icon = Icons.Filled.AccountBox, onClick = onChangeLicense)

        Spacer(modifier = Modifier.height(8.dp))

        Text("Account Management", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        // Logout
        SettingsItem(label = "Logout", icon = Icons.AutoMirrored.Filled.ArrowBack, onClick = onLogout)

        // Clear Exam History
        SettingsItem(label = "Clear Mock Exam History", icon = Icons.Default.Delete, onClick = onClearExams)

        // Clear HPT History
        SettingsItem(label = "Clear Hazard Test History", icon = Icons.Default.Delete, onClick = onClearHpt)

        // Delete Account
        SettingsItem(label = "Delete Account", icon = Icons.Default.Delete, onClick = onDeleteAccount, isDestructive = true)
    }
}

@Composable
fun SettingsItem(label: String, icon: ImageVector, onClick: () -> Unit, isDestructive: Boolean = false) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDestructive) Color(0xFFFFEBEE) else Color.White
        )
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isDestructive) Color.Red else Color.Gray)
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = if (isDestructive) Color.Red else Color.Black)
        }
    }
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
            pushJsonToFirestore(context)
        }) {
            Text("Import JSON HPTs")
        }
    }
}

fun clearExamHistory(userId: String) {
    Firebase.database.getReference("users/$userId/examHistory").removeValue()
}

fun clearHptHistory(userId: String) {
    Firebase.database.getReference("users/$userId/hptHistory").removeValue()
}

fun updateExamDate(userId: String, newDate: String) {
    Firebase.database.getReference("users/$userId/examDate").setValue(newDate)
}

fun updateLicenseType(userId: String, newLicense: String) {
    Firebase.database.getReference("users/$userId/licenseType").setValue(newLicense)
}

@Composable
fun emptySpace()
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Space is Empty.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }

}

data class Award(val name: String, val description: String, val requirement: Int, val color: Color)

@Composable
fun AwardCard(award: Award, isUnlocked: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(if (isUnlocked) 4.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).alpha(if (isUnlocked) 1f else 0.5f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Trophy Icon with Dynamic Color
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (isUnlocked) award.color else Color.Gray,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = award.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isUnlocked) "Unlocked: ${award.description}" else "Locked: Reach ${award.requirement} day streak",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            if (isUnlocked) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Unlocked", tint = Color(0xFF2E7D32))
            } else {
                Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.LightGray)
            }
        }
    }
}

@Composable
fun AwardsTab(maxStreak: Int, perfectScoreCount: Int, examPassCount: Int) {
    val streakLocked = "A"
    val perfectLocked = "B"
    val examLocked = "C"

    // --- TROPHY 1: STREAK MASTERY ---
    val (tierName, tierColor, nextGoal) = when {
        maxStreak >= 30 -> Triple("Gold Master", Color(0xFFFFD700), 0)
        maxStreak >= 20 -> Triple("Silver Striker", Color(0xFFC0C0C0), 30)
        maxStreak >= 10 -> Triple("Bronze Beginner", Color(0xFFCD7F32), 20)
        else -> Triple("No Trophy Yet", Color.LightGray, 10)
    }

    // --- TROPHY 2: PRECISION MASTERY ---
    val (pName, pColor, pGoal) = when {
        perfectScoreCount >= 6 -> Triple("Gold Sharpshooter", Color(0xFFFFD700), 0)
        perfectScoreCount >= 3 -> Triple("Silver Spotter", Color(0xFFC0C0C0), 6)
        perfectScoreCount >= 1 -> Triple("Bronze Watcher", Color(0xFFCD7F32), 3)
        else -> Triple("Precision Locked", Color.LightGray, 1)
    }

    // --- TROPHY 2: EXAM MASTERY ---
    val (eName, eColor, eGoal) = when {
        examPassCount >= 7 -> Triple("LTO Professor", Color(0xFFFFD700), 0) // Mastered all topics
        examPassCount >= 4 -> Triple("Road Scholar", Color(0xFFC0C0C0), 7)
        examPassCount >= 1 -> Triple("License Ready", Color(0xFFCD7F32), 4)
        else -> Triple("Exam Locked", Color.LightGray, 1)
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔥 THE EVOLVING TROPHY CARD
        LevelUpTrophyCard(tierName, tierColor, maxStreak, nextGoal, lockedAt = streakLocked, icon = Icons.Default.Place)

        Spacer(modifier = Modifier.height(16.dp))

        LevelUpTrophyCard(
            name = pName,
            color = pColor,
            streak = perfectScoreCount, // Using count instead of streak here
            nextGoal = pGoal,
            lockedAt = perfectLocked,
            icon = Icons.Default.Star
        )

        Spacer(modifier = Modifier.height(16.dp))

        LevelUpTrophyCard(
            name = eName,
            color = eColor,
            streak = examPassCount, // Using count instead of streak here
            nextGoal = eGoal,
            lockedAt = examLocked,
            icon = Icons.Default.CheckCircle
        )
    }
}

@Composable
fun LevelUpTrophyCard(
    name: String,
    color: Color,
    streak: Int,
    nextGoal: Int,
    lockedAt: String,
    icon: ImageVector = Icons.Default.Email
) {
    var isLocked by remember { mutableStateOf(false) }
    when (lockedAt){
        "A" -> isLocked = streak < 10
        "B" -> isLocked = streak < 1
        "C" -> isLocked = streak < 1
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(if (isLocked) 0.dp else 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🏆 The Dynamic Icon
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .size(100.dp)
                    // Add a slight "Glow" if it's Gold
                    .graphicsLayer(alpha = if (isLocked) 0.3f else 1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = name.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = if (isLocked) Color.Gray else Color.Black
            )

            if (lockedAt == "A"){
                Text(
                    text = "Personal Best: $streak Days",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            if (lockedAt == "B")
            {
                Text(
                    text = "HPT: $streak Perfect Scores in a Row",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            if (lockedAt == "C")
            {
                Text(
                    text = "Passed $streak Exams",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            LinearProgressIndicator(
                progress = { streak.toFloat() / nextGoal },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = color, // Progress bar matches the trophy color
                trackColor = Color(0xFFF0F0F0)
            )

            if(lockedAt == "A"){
                if (nextGoal > 0) {
                    Text(
                        "${nextGoal - streak} more to reach next tier",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp),
                        color = Color.Gray
                    )
                }
            }
            if(lockedAt == "B"){
                if (nextGoal > 0) {
                    Text(
                        "${nextGoal - streak} more perfect scores to reach next tier",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp),
                        color = Color.Gray
                    )
                }
            }
            if(lockedAt == "C"){
                if (nextGoal > 0) {
                    Text(
                        "Pass ${nextGoal - streak} more exams to reach next tier",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 4.dp),
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun TrophyEarnedBanner(visible: Boolean, message: String, onDismiss: () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .statusBarsPadding(), // Ensures it doesn't overlap system icons
            color = Color(0xFFFFD700), // Gold Color
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🥳",
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                // CLOSE BUTTON
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.Black)
                }
            }
        }
    }
}