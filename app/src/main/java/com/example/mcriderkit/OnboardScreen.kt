package com.example.mcriderkit

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import java.util.Calendar

@Composable
fun OnboardScreen(navController: NavHostController) {
    // Upgraded state machine to support Step 3 (The App Feature Guide)
    var step by remember { mutableIntStateOf(1) }
    val context = LocalContext.current

    // State for Step 1
    var selectedLicense by remember { mutableStateOf("") }

    // State for Step 2
    var examDate by remember { mutableStateOf("Select Date") }
    val calendar = Calendar.getInstance()


    val onboardSlides = listOf(
        R.drawable.homepage_tutorial_1,
        R.drawable.homepage_tutorial_2,
        R.drawable.homepage_tutorial_3,
        R.drawable.homepage_tutorial_4
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            examDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // --- BASE LAYER: ONBOARDING STEPS ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Progress Indicator hidden gracefully if showing UI Guide overlay
            if (step <= 2) {
                LinearProgressIndicator(
                    progress = { if (step == 1) 0.5f else 1f },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                )
            }

            if (step == 1) {
                Text("Which license are you targeting?", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(24.dp))

                LicenseCard("Non-Professional", "For private vehicles", selectedLicense == "Non-Professional") {
                    selectedLicense = "Non-Professional"
                }
                Spacer(modifier = Modifier.height(12.dp))
                LicenseCard("Professional", "For for-hire/heavy vehicles", selectedLicense == "Professional") {
                    selectedLicense = "Professional"
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { step = 2 },
                    enabled = selectedLicense.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Next") }

            } else if (step == 2) {
                Text("When is your exam date?", style = MaterialTheme.typography.headlineSmall)
                Text("We'll help you pace your study.", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedButton(onClick = { datePickerDialog.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(examDate)
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        // Instead of immediate navigation, transition cleanly into the interactive UI Coach
                        step = 3
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Continue") }
            } else {
                // Background placeholder behind the overlay so the UI doesn't look completely blank
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Preparing your dashboard...", color = Color.Gray)
                }
            }
        }

        // --- OVERLAY LAYER: MINI UI FEATURE GUIDE ---
        AnimatedVisibility(
            visible = step == 3,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CanvaUiGuideOverlay(
                onGuideComplete = {
                    saveOnboardingData(selectedLicense, examDate, navController)
                },
                slideImages = onboardSlides,
                finishButtonText = "Let's Go!",
            )
        }
    }
}

@Composable
fun UiGuideOverlay(onGuideComplete: () -> Unit) {
    var guidePage by remember { mutableIntStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.82f)) // Dark dimming overlay effect
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Quick Badge Indicator
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Quick Tour: $guidePage/3",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Dynamic Content card swapping text based on internal step counters
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    when (guidePage) {
                        1 -> {
                            Text("📚 Mock Exam Engine", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Practice with randomized questions modeled exactly around the official LTO questionnaire. Review errors inside your history panel instantly.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        2 -> {
                            Text("🗺️ Road Signs Index", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Browse clean, high-resolution listings categorized by Regulatory, Warning, and Information road instructions to nail visual criteria.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        3 -> {
                            Text("⏱️ Exam Smart Pacing", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Based on the target date you provided, we will prioritize custom study reminders inside your dashboard timeline to keep you on schedule.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                if (guidePage < 3) {
                                    guidePage++
                                } else {
                                    onGuideComplete()
                                }
                            }
                        ) {
                            Text(if (guidePage == 3) "Got It, Let's Go!" else "Next Feature")
                        }
                    }
                }
            }
        }
    }
}

// Keeping your existing components safe and unchanged below...
@Composable
fun LicenseCard(title: String, subtitle: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF2A6CF6) else Color.White
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = if (isSelected) Color.White else Color.Black)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray)
        }
    }
}

fun saveOnboardingData(license: String, date: String, navController: NavHostController) {
    val userId = Firebase.auth.currentUser?.uid ?: return
    val db = Firebase.database.getReference("users/$userId")

    val updates = mapOf(
        "licenseType" to license,
        "examDate" to date,
        "onboardingComplete" to true
    )

    db.updateChildren(updates).addOnSuccessListener {
        navController.navigate("main") {
            popUpTo("onboard") { inclusive = true } // Corrected typo from "onboarding" to match screen names securely
        }
    }
}