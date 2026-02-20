package com.example.mcriderkit.exam

import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.firestore

data class QuizType(
    val title: String,
    val desc: String,
    val targetCategory: String,
    val icon: ImageVector,
    val qCount: Int,      // Number of questions
    val passPct: String,   // Passing requirement text
    val rank: String
)

@OptIn(UnstableApi::class)
@Composable
fun QuizMenuScreen(navController: NavHostController) {
    var categoryCounts by remember { mutableStateOf(mapOf<String, Int>()) }
    var userLicenseType by remember { mutableStateOf("Non-Professional") }
    val userId = Firebase.auth.currentUser?.uid

    LaunchedEffect(userId) {
        if (userId != null) {
            val userRef = Firebase.database.getReference("users/$userId/licenseType")

            userRef.get().addOnSuccessListener { snapshot ->
                val type = snapshot.getValue(String::class.java) ?: "Non-Professional"
                userLicenseType = type

                // ✅ THIS WILL LOG THE CORRECT VALUE
                Log.d("Debug", "Firebase returned: $userLicenseType")
            }.addOnFailureListener {
                Log.e("Debug", "Firebase Error: ${it.message}")
            }
        }
    }

    // --- Data Setup ---
    val quizTypes = remember(userLicenseType) {
        val baseList = mutableListOf(
            QuizType("General Knowledge", "Laws, rules, and etiquette", "General Knowledge", Icons.Default.Check, 20, "80%", "1"),
            QuizType("Road Signs", "Practice specifically for signs", "Road Signs", Icons.Default.Check, 20, "80%", "2"),
            QuizType("Road Courtesy", "Practice driving safely", "Road Courtesy", Icons.Default.Check, 15, "80%", "3"),
            QuizType("Driving", "Practice Theoretical Driving Scenarios", "Driving", Icons.Default.Check, 20, "80%", "4"),
            QuizType("Emergencies", "Practice emergency procedures", "Emergencies", Icons.Default.Check, 20, "80%", "5"),
            QuizType("Fines & Penalties", "Memorize violations and costs", "Fines and Penalties", Icons.Default.Check, 15, "80%", "6")
        )

        // 🚀 Add Professional-only categories if applicable
        if (userLicenseType == "Professional") {
            baseList.add(QuizType("Passenger & Public Safety", "Public transport rules", "Passenger & Public Safety", Icons.Default.Check, 20, "80%", "7"))
            baseList.add(QuizType("Commercial Vehicle Operation", "Heavy vehicle protocols", "Commercial Vehicle Operation", Icons.Default.Check, 20, "80%", "8"))
        }

        // Always put the Full Mock Exam at the very end
        baseList.add(QuizType("Full Mock Exam", "60 random questions (All topics)", "All", Icons.Default.Check, 60, "80%", (baseList.size + 1).toString()))

        baseList.toList()
    }

    LaunchedEffect(Unit) {
        val db = Firebase.firestore

        val quizCategories = if(userLicenseType == "Non-Professional"){
            listOf("General Knowledge", "Road Signs", "Road Courtesy", "Driving", "Emergencies", "Fines and Penalties")
        }else{
            listOf("General Knowledge", "Road Signs", "Road Courtesy", "Driving", "Emergencies", "Fines and Penalties", "Passenger & Public Safety", "Commercial Vehicle Operation")
        }

        val countsMap = mutableMapOf<String, Int>()
        countsMap["All"] = 60

        // 🚀 Fetch count for each specific category
        quizCategories.forEach { category ->
            db.collection("questions")
                .whereEqualTo("category", category)
                .count() // ⚡ Fast & Cheap Aggregation Query
                .get(AggregateSource.SERVER)
                .addOnSuccessListener { snapshot ->
                    countsMap[category] = snapshot.count.toInt()
                    // Update state once we have all or as they come in
                    categoryCounts = countsMap.toMap()
                }
        }
    }


    val quizOrder = quizTypes.map { it.targetCategory }
    var bestScores by remember { mutableStateOf(mapOf<String, Int>()) }

    // --- Data Fetching ---
    LaunchedEffect(userId) {
        if (userId != null) {
            val historyRef = Firebase.database.getReference("users/$userId/examHistory")
            historyRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val scoresMap = mutableMapOf<String, Int>()
                    snapshot.children.forEach { child ->
                        val type = child.child("examType").getValue(String::class.java) ?: ""
                        val pct = child.child("percentage").getValue(Int::class.java) ?: 0
                        if (pct > (scoresMap[type] ?: -1)) scoresMap[type] = pct
                    }
                    bestScores = scoresMap
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F9FA)).padding(top = 16.dp)) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            Text("Select Quiz Type", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text("Unlock categories by passing previous tests", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // FIX: Ensure the items block is correctly scoped
            items(quizTypes) { quiz ->
                val bestPct = bestScores[quiz.targetCategory]
                val quizIndex = quizOrder.indexOf(quiz.targetCategory)
                val dynamicCount = categoryCounts[quiz.targetCategory] ?: 0

                // --- IMPROVED UNLOCK LOGIC ---
                // A quiz is unlocked if it's the first one OR if the one right before it is passed (>= 80)
                val isUnlocked = if (quizIndex == 0) true
                else (bestScores[quizOrder[quizIndex - 1]] ?: 0) >= 80

                Card(
                    onClick = {
                        if (isUnlocked) {
                            // FIX: Correct string interpolation
                            navController.navigate("quiz/${quiz.targetCategory}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .graphicsLayer(alpha = if (isUnlocked) 1f else 0.6f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(if (isUnlocked) 2.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icon Circle
                        Surface(
                            color = when {
                                !isUnlocked -> Color(0xFFF1F3F4)
                                bestPct != null && bestPct >= 80 -> Color(0xFFD9EAD3)
                                else -> Color(0xFFE8F0FF)
                            },
                            shape = CircleShape,
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                when {
                                    !isUnlocked -> Icon(Icons.Default.Lock, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                                    bestPct != null && bestPct >= 80 -> Icon(Icons.Default.Check, null, tint = Color(0xFF2E7D32))
                                    else -> Text(quiz.rank, fontWeight = FontWeight.Bold, color = Color(0xFF1A4FD9))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(quiz.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(quiz.desc, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Filled.List, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                                Text(" $dynamicCount Questions", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(12.dp), tint = Color.Gray)
                                Text("Pass: ${quiz.passPct}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)

                                if (bestPct != null) {
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        "Best: $bestPct%",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (bestPct >= 80) Color(0xFF2E7D32) else Color.Red
                                    )
                                }
                            }
                        }

                        Icon(
                            imageVector = if (isUnlocked) Icons.AutoMirrored.Filled.KeyboardArrowRight else Icons.Default.Lock,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
