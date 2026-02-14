package com.example.mcriderkit.exam

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.mcriderkit.data.Question
import com.example.mcriderkit.data.checkAndAwardTrophies
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay

enum class ExamPhase { TESTING, RESULTS, REVIEW }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockExamScreen(navController: NavHostController, category: String) {
    val db = Firebase.firestore
    // Language
    var isTagalog by remember { mutableStateOf(false) }

    // Determine if this is a "Formal" exam or a "Practice" session
    val isFormalExam = category == "All"

    // 1. Exam Data State
    var questions by remember { mutableStateOf(listOf<Question>()) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswers = remember { mutableStateMapOf<Int, Int>() } // QuestionIndex -> SelectedChoiceIndex
    var isLoading by remember { mutableStateOf(true) }

    // Timer state
    var timeLeft by remember { mutableIntStateOf(if (isFormalExam) 60 * 60 else 0) }
    var isExamFinished by remember { mutableStateOf(false) }

    // Change your boolean to a Phase state
    var currentPhase by remember { mutableStateOf(ExamPhase.TESTING) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    var showExitDialog by remember { mutableStateOf(false) }
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Exam?") },
            text = { Text("Your progress will not be saved. Are you sure you want to exit?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    navController.popBackStack() // Go back to Home
                }) {
                    Text("Quit", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("Cancel") }
            }
        )
    }

    // 1. Fetch and Shuffle 50 Questions and Randomize the order of the choices
    LaunchedEffect(Unit) {
        db.collection("questions").get().addOnSuccessListener { result ->
            val allQuestions = result.toObjects(Question::class.java)

            // 1. Create the filtered list inside the listener
            val filteredList = if (category == "All") {
                val fines = allQuestions.filter { it.category == "Fines and Penalties" }.shuffled().take(5)
                val others = allQuestions.filter { it.category != "Fines and Penalties" }.shuffled().take(45)
                (fines + others).shuffled()
            } else {
                allQuestions.filter { it.category == category }.shuffled().take(20)
            }

            // 2. Map and randomize choices immediately after filtering
            questions = filteredList.map { question ->
                val originalChoices = question.choices
                val correctText = originalChoices.getOrNull(question.answerIndex) ?: ""

                val shuffledChoices = originalChoices.shuffled()
                val newIndex = shuffledChoices.indexOf(correctText)

                question.copy(
                    choices = shuffledChoices,
                    answerIndex = newIndex
                )
            }
            // 3. Stop loading only AFTER the mapping is done
            isLoading = false
        }.addOnFailureListener {
            isLoading = false // Ensure loading stops even on error
        }
    }

    // 2. Conditional Timer LaunchedEffect
    LaunchedEffect(key1 = timeLeft, key2 = isExamFinished) {
        // Only run the countdown if it's a formal exam and time is > 0
        if (isFormalExam && timeLeft > 0 && !isExamFinished) {
            delay(1000L)
            timeLeft--
        } else if (isFormalExam && timeLeft == 0) {
            isExamFinished = true
        }
    }

    when (currentPhase) {
        ExamPhase.TESTING -> {
            ExamContent(
                questions = questions,
                currentIndex = currentIndex,
                timeLeft = if (isFormalExam) timeLeft else -1,
                selectedAnswers = selectedAnswers,
                onNext = { if (currentIndex < questions.size - 1) currentIndex++ },
                onPrev = { if (currentIndex > 0) currentIndex-- },
                onOpenGrid = { showSheet = true },
                isTagalog = isTagalog,
                onLanguageToggle = { isTagalog = it },
                onExitClick = { showExitDialog = true },
                onFinish = {
                    // 1. Calculate the score right here
                    val score = questions.filterIndexed { index, question ->
                        selectedAnswers[index] == question.answerIndex
                    }.size
                    val total = questions.size
                    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0
                    val isPassed = percentage >= 80

                    // 2. Save to Firebase IMMEDIATELY (Only happens once)
                    val userId = Firebase.auth.currentUser?.uid
                    if (userId != null) {
                        val examData = mapOf(
                            "score" to score,
                            "total" to total,
                            "percentage" to percentage,
                            "date" to System.currentTimeMillis(),
                            "passed" to isPassed,
                            "examType" to category
                        )
                        Firebase.database.getReference("users/$userId/examHistory")
                            .push().setValue(examData)
                    }
                    if (userId != null && isPassed) {
                        val database = FirebaseDatabase.getInstance().getReference("users/$userId")
                        database.get().addOnSuccessListener { snapshot ->
                            val masteredExams = snapshot.child("masteredExams").children.mapNotNull {
                                it.value as? String
                            }.toMutableList()
                            // 🚀 ONLY increment if this category hasn't been mastered yet
                            if (!masteredExams.contains(category)) {
                                masteredExams.add(category)
                                val newExamPassCount = masteredExams.size

                                val currentStreak = snapshot.child("streakCount").getValue(Int::class.java) ?: 0
                                val currentPerfects = snapshot.child("perfectScoreCount").getValue(Int::class.java) ?: 0

                                val updates = mapOf(
                                    "masteredExams" to masteredExams,
                                    "examPassCount" to newExamPassCount
                                )
                                database.updateChildren(updates).addOnSuccessListener {
                                    checkAndAwardTrophies(
                                        userId = userId,
                                        newStreak = currentStreak,
                                        newPerfectCount = currentPerfects,
                                        newExamPassCount = newExamPassCount
                                    )
                                }
                            }
                        }
                    }

                    currentPhase = ExamPhase.RESULTS
                }
            )
        }

        ExamPhase.RESULTS -> {
            ResultSummary(
                questions = questions,
                selectedAnswers = selectedAnswers,
                onReviewMistakes = { currentPhase = ExamPhase.REVIEW }, // Switch to Review
                onExit = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        ExamPhase.REVIEW -> {
            // new ReviewMistakes UI
            // Add a back button: onClick = { currentPhase = ExamPhase.RESULTS }
            ReviewMistakesContent(
                questions = questions,
                selectedAnswers = selectedAnswers,
                onBackToResults = { currentPhase = ExamPhase.RESULTS }
            )
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState
        ) {
            Text(
                "Jump to Question",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            QuestionGrid(
                total = questions.size,
                currentIndex = currentIndex,
                selectedAnswers = selectedAnswers,
                onSelect = { index ->
                    currentIndex = index
                    showSheet = false // Close after clicking
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamContent(
    questions: List<Question>,
    currentIndex: Int,
    timeLeft: Int,
    selectedAnswers: MutableMap<Int, Int>,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onFinish: () -> Unit,
    onOpenGrid: () -> Unit,
    isTagalog: Boolean,
    onLanguageToggle: (Boolean) -> Unit,
    onExitClick: () -> Unit
) {
    // 1. Get the specific question for the current index
    val currentQuestion = questions.getOrNull(currentIndex) ?: return

    // Logic to select the right language
    val displayText = if (isTagalog && currentQuestion.textTL.isNotEmpty())
        currentQuestion.textTL else currentQuestion.text

    val displayChoices = if (isTagalog && currentQuestion.choicesTL.isNotEmpty())
        currentQuestion.choicesTL else currentQuestion.choices

    Scaffold(
        topBar = {
            // Reusing your existing TopBar
            ExamTopBar(
                timeLeft,
                currentIndex,
                questions.size,
                isTagalog,
                onLanguageToggle = onLanguageToggle,
                onExitClick = onExitClick)
        },
        bottomBar = {
            // Reusing your existing Navigation
            ExamNavigationButtons(
                currentIndex = currentIndex,
                total = questions.size,
                onNext = onNext,
                onPrev = onPrev,
                onSubmit = onFinish,
                onOpenGrid = onOpenGrid
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 2. Question Text
            Text(
                text = "${currentIndex + 1}. ${displayText}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // 3. Question Image
            if (currentQuestion.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = currentQuestion.imageUrl,
                    contentDescription = "Exam Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Choices Logic
            displayChoices.forEachIndexed { index, choice ->
                val isSelected = selectedAnswers[currentIndex] == index

                OutlinedCard(
                    onClick = { selectedAnswers[currentIndex] = index },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isSelected) Color(0xFF2A6CF6) else Color.LightGray
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { selectedAnswers[currentIndex] = index }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = choice)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamTopBar(timeLeft: Int,
               currentIndex: Int,
               total: Int,
               isTagalog: Boolean,
               onLanguageToggle: (Boolean) -> Unit,
               onExitClick: () -> Unit) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        navigationIcon = {
            IconButton(onClick = onExitClick) {
                Icon(Icons.Default.Close, contentDescription = "Exit Exam")
            }
        },
        title = { Text("Exam: ${currentIndex + 1}/$total") },
        actions = {
            // Language Switch
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isTagalog) "PH" else "EN",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
                Switch(
                    checked = isTagalog,
                    onCheckedChange = onLanguageToggle,
                    modifier = Modifier.scale(0.7f).padding(horizontal = 4.dp)
                )
            }

            // Timer
            if (timeLeft >= 0) {
                val mins = timeLeft / 60
                val secs = timeLeft % 60
                Text(
                    text = String.format("%02d:%02d", mins, secs),
                    modifier = Modifier.padding(end = 16.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (timeLeft < 300) Color.Red else Color.Black
                )
            }
        }
    )
}

@Composable
fun ExamNavigationButtons(
    currentIndex: Int,
    total: Int,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSubmit: () -> Unit,
    onOpenGrid: () -> Unit // Add this
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Keeps everything aligned
    ) {
        // Back Button
        Button(onClick = onPrev, enabled = currentIndex > 0) {
            Text("Back")
        }

        // Grid View Button (In the middle)
        Button(onClick = onOpenGrid, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A6CF6))) {
            Text(text = "Jump to Question")
        }

        // Next or Finish Button
        if (currentIndex == total - 1) {
            Button(
                onClick = onSubmit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Finish Exam")
            }
        } else {
            Button(onClick = onNext) {
                Text("Next")
            }
        }
    }
}

@Composable
fun QuestionGrid(
    total: Int,
    currentIndex: Int,
    selectedAnswers: Map<Int, Int>,
    onSelect: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5), // 5 numbers per row
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(total) { index ->
            val isAnswered = selectedAnswers.containsKey(index)
            val isCurrent = currentIndex == index

            OutlinedCard(
                onClick = { onSelect(index) },
                colors = CardDefaults.outlinedCardColors(
                    containerColor = when {
                        isCurrent -> Color(0xFF2A6CF6) // Blue for current
                        isAnswered -> Color(0xFFE8F5E9) // Light Green for answered
                        else -> Color.White
                    }
                ),
                border = BorderStroke(
                    1.dp,
                    if (isCurrent) Color(0xFF2A6CF6) else Color.LightGray
                )
            ) {
                Box(Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = "${index + 1}",
                        color = if (isCurrent) Color.White else Color.Black,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
