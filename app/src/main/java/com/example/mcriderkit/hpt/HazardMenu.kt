package com.example.mcriderkit.hpt

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mcriderkit.R
import com.example.mcriderkit.data.HazardClip
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

@SuppressLint("DiscouragedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HazardMenuScreen(navController: NavHostController) {
    val db = Firebase.firestore
    val context = LocalContext.current

    val userId = Firebase.auth.currentUser?.uid
    var bestScores by remember { mutableStateOf(mapOf<String, Int>()) }

    // State to hold the clips fetched from Firestore
    var hazardClips by remember { mutableStateOf<List<HazardClip>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (userId != null) {
            // Fetch data from the "hazard_clips" collection
            db.collection("hazard_clips")
                .get()
                .addOnSuccessListener { result ->
                    // Map Firestore documents to our HazardClip data class
                    hazardClips = result.toObjects(HazardClip::class.java)
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Error fetching clips: ${e.message}")
                    isLoading = false
                }

            // 2. Fetch Best Scores from Realtime Database
            val historyRef = Firebase.database.getReference("users/$userId/hptHistory")
            historyRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val scores = mutableMapOf<String, Int>()

                    snapshot.children.forEach { child ->
                        // 1. Check if the test was a Daily Challenge
                        // Use the exact key name you used in your save logic (isDaily or Daily)
                        val isDaily = child.child("Daily").getValue(Boolean::class.java) ?: false

                        // 🚀 ONLY process if it's NOT a daily challenge
                        if (!isDaily) {
                            val cid = child.child("clipId").getValue(String::class.java) ?: ""
                            val score = child.child("score").getValue(Int::class.java) ?: 0

                            // 2. Keep only the highest score found for this specific clipId
                            if (score > (scores[cid] ?: -1)) {
                                scores[cid] = score
                            }
                        }
                    }
                    bestScores = scores
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }




    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Hazard Perception Clips") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("hazard") }) {
                    Icon(Icons.Default.Close, contentDescription = "Back")
                }
            }
        ) }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(hazardClips) { clip ->
                    HazardClipCard(
                        clip = clip,
                        bestScore = bestScores[clip.id] // Look up score by ID
                    ) {
                        val resId = context.resources.getIdentifier(clip.videoFileName, "raw", context.packageName)
                        navController.navigate("hazard_player/$resId/false")
                    }
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun HazardClipCard(clip: HazardClip, bestScore: Int?, onClick: () -> Unit) {
    val context = LocalContext.current

    val thumbnailResId = remember(clip.id) {
        context.resources.getIdentifier(
            "thumbnail_${clip.id}",
            "drawable",
            context.packageName
        ).let { if (it == 0) R.drawable.thumbnail_hpt1 else it } // Fallback image
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. THUMBNAIL TEMPLATE
            Box(
                modifier = Modifier
                    .size(width = 100.dp, height = 64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F2F5)), // Placeholder background
                contentAlignment = Alignment.Center
            ) {
                // If you have actual images, use AsyncImage here.
                // For now, we use a styled "Video" icon look.
                Image (
                    painter = painterResource(id = thumbnailResId),
                    contentDescription = "Thumbnail",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // ✅ MASTERY OVERLAY (If score is 5/5)
                if (bestScore == 5) {
                    Surface(
                        color = Color(0xFF2E7D32),
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 2. TEXT CONTENT
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = clip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            // 3. PERSONAL BEST BADGE
            if (bestScore != null) {
                Surface(
                    color = if (bestScore >= 3) Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "$bestScore/5",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (bestScore >= 4) Color(0xFF2E7D32) else Color.DarkGray
                        )
                        Text(
                            text = "BEST",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (bestScore >= 4) Color(0xFF2E7D32) else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
