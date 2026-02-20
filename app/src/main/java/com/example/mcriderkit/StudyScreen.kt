package com.example.mcriderkit

import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.mcriderkit.data.Question
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore


@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyScreen(navController: NavHostController) {
    val db = Firebase.firestore
    var questions by remember { mutableStateOf(listOf<Question>()) }
    var selectedCategory by remember { mutableStateOf("All") }
    var isLoading by remember { mutableStateOf(true) }
    var isTagalog by remember { mutableStateOf(false) }

    var masteredIds by remember { mutableStateOf(setOf<String>()) }
    val userId = Firebase.auth.currentUser?.uid
    val database = Firebase.database.getReference("users").child(userId ?: "anonymous").child("masteredQuestions")
    var bookmarkedIds by remember { mutableStateOf(setOf<String>()) }
    val bookmarkRef = Firebase.database.getReference("users").child(userId ?: "anonymous").child("bookmarkedQuestions")

    var showOnlyStarred by remember { mutableStateOf(false) }
    var hideMastered by remember { mutableStateOf(false) }
    var showOnlyMastered by remember { mutableStateOf(false) }
    var userLicenseType by remember { mutableStateOf("Non-Professional") }

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

    val categories = if (userLicenseType == "Non-Professional")
    {
        listOf("All", "General Knowledge", "Road Signs", "Fines and Penalties", "Driving", "Emergencies", "Road Courtesy" )
    }else{
        listOf("All", "General Knowledge", "Road Signs", "Fines and Penalties", "Driving", "Emergencies", "Road Courtesy", "Passenger & Public Safety", "Commercial Vehicle Operation")
    }


    LaunchedEffect(Unit) {
        // Fetch Firestore Questions
        db.collection("questions")
            .get()
            .addOnSuccessListener { result ->
                questions = result.toObjects(Question::class.java)
                isLoading = false
            }
            .addOnFailureListener {
                isLoading = false
            }

        // FETCH MASTERY FROM REALTIME DB
        database.get().addOnSuccessListener { snapshot ->
            val ids = snapshot.children.mapNotNull { it.key }.toSet()
            masteredIds = ids
        }

        // Fetch existing stars
        bookmarkRef.get().addOnSuccessListener { snapshot ->
            bookmarkedIds = snapshot.children.mapNotNull { it.key }.toSet()
        }
    }

    val toggleMastery: (String, Boolean) -> Unit = { qId, isChecked ->
        if (isChecked) {
            database.child(qId).setValue(true)
            masteredIds = masteredIds + qId
        } else {
            database.child(qId).removeValue()
            masteredIds = masteredIds - qId
        }
    }

    // Function to toggle the Star
    val toggleBookmark: (String, Boolean) -> Unit = { qId, isStarred ->
        if (isStarred) {
            bookmarkRef.child(qId).setValue(true)
            bookmarkedIds = bookmarkedIds + qId
        } else {
            bookmarkRef.child(qId).removeValue()
            bookmarkedIds = bookmarkedIds - qId
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = "Study",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                    ) },
                actions = {
                    // The Language Toggle Switch
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            if (isTagalog) "PH" else "EN",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Switch(
                            checked = isTagalog,
                            onCheckedChange = { isTagalog = it },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding -> // 1. Use the padding provided by Scaffold
        Column(modifier = Modifier.padding(innerPadding)) {

            // 2. The Category Tabs
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory),
                edgePadding = 16.dp,
                containerColor = Color.White
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = (selectedCategory == category),
                        onClick = { selectedCategory = category },
                        text = { Text(text = category) }
                    )
                }
            }

            val chipScrollState = rememberScrollState()
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)
                    .horizontalScroll(chipScrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Star Filter Chip
                FilterChip(
                    selected = showOnlyStarred,
                    onClick = { showOnlyStarred = !showOnlyStarred },
                    label = { Text("Starred: ${bookmarkedIds.size}") },
                    leadingIcon = { Icon(Icons.Default.Star, contentDescription = null,
                        modifier = Modifier.size(18.dp)) }
                )

                // Show Mastered Chip
                FilterChip(
                    selected = showOnlyMastered,
                    onClick = {
                        showOnlyMastered = !showOnlyMastered
                        if (showOnlyMastered) hideMastered = false // Turn off conflicting filter
                    },
                    label = { Text("Mastered: ${masteredIds.size}") }
                )

                // Hide Mastered Filter Chip
                FilterChip(
                    selected = hideMastered,
                    onClick = {
                        hideMastered = !hideMastered
                        if (hideMastered) showOnlyMastered  = false // Turn off conflicting filter
                    },
                    label = { Text("Hide Mastered") }
                )
            }

            // 3. Filtering logic (explicitly naming 'question' fixes the 'it' error)
            val filteredQuestions = questions.filter { question: Question ->
                // Check Category: true if "All" is selected OR if category matches
                val matchesCategory = if (selectedCategory == "All") true
                else question.category == selectedCategory

                // Check Starred: true if filter is OFF OR if the ID is in our bookmarkedIds set
                val matchesStarFilter = if (showOnlyStarred) bookmarkedIds.contains(question.id)
                else true

                val isQuestionMastered = masteredIds.contains(question.id)
                val matchesMasteryFilter = when {
                    showOnlyMastered -> isQuestionMastered      // Show ONLY mastered
                    hideMastered -> !isQuestionMastered         // Show ONLY unmastered
                    else -> true                                // Show EVERYTHING
                }

                // If hideMastered is ON, only show questions NOT in masteredIds
                // Only include the question if BOTH conditions are true
                matchesCategory && matchesStarFilter && matchesMasteryFilter
            }

            // 4. The Content
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (filteredQuestions.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (showOnlyStarred) "No starred questions in this category."
                            else "No questions found.",
                            color = Color.Gray
                        )

                    }
                }
                else{
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)) {
                        items(filteredQuestions) { question -> // 5. filtered list
                            QuestionReviewCard(
                                question = question,
                                isTagalog = isTagalog,
                                isMastered = masteredIds.contains(question.id),
                                isStarred = bookmarkedIds.contains(question.id),
                                onMasteryChange = { checked -> toggleMastery(question.id, checked) },
                                onStarChange = { starred -> toggleBookmark(question.id, starred) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionReviewCard(question: Question,
                       isTagalog: Boolean,
                       isMastered: Boolean,
                       isStarred: Boolean,
                       onMasteryChange: (Boolean) -> Unit,
                       onStarChange: (Boolean) -> Unit) {
    val displayText = if (isTagalog && question.textTL.isNotEmpty()) question.textTL else question.text
    val displayChoices = if (isTagalog && question.choicesTL.isNotEmpty()) question.choicesTL else question.choices
    val displayExplanation = if (isTagalog && !question.explanationTL.isNullOrBlank()) question.explanationTL else question.explanation


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isStarred) Color(0xFFFFFBE6) else Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 1. Category Tag
            Surface(
                color = Color(0xFFE3F2FD),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = question.category,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF1976D2)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Question Text
            Text(
                text = displayText,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            // 3. Image (Only if URL is not empty)
            if (question.imageUrl.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = question.imageUrl,
                    contentDescription = "MCRiderKit-Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. The Correct Answer (Highlighted)
            val correctAnswer = displayChoices.getOrNull(question.answerIndex) ?: ""
            Text(
                text = if (isTagalog) "Sagot: $correctAnswer" else "Answer: $correctAnswer",
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Bold
            )

            // 5. The "Why" Explanation
            if (!displayExplanation.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isTagalog) "Bakit: $displayExplanation" else "Why: $displayExplanation",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // 4. BOTTOM ACTION ROW: Controls
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                // Left side: Star/Bookmark toggle
                TextButton(
                    onClick = { onStarChange(!isStarred) } // Pass the REVERSE of current state
                ) {
                    Icon(
                        imageVector = if (isStarred) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (isStarred) Color(0xFFFFB300) else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isStarred) "Starred" else "Star",
                        color = if (isStarred) Color(0xFFFFB300) else Color.Gray
                    )
                }
                // Right side: Mastered Checkbox
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isTagalog) "Kabisado na" else "Mastered",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isMastered) Color(0xFF2E7D32) else Color.Gray
                    )
                    Checkbox(
                        checked = isMastered,
                        onCheckedChange = onMasteryChange,
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF2E7D32))
                    )
                }
            }
        }
    }
}
