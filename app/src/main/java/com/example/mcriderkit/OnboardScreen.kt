package com.example.mcriderkit

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var step by remember { mutableIntStateOf(1) }
    val context = LocalContext.current


    // State for Step 1
    var selectedLicense by remember { mutableStateOf("") }

    // State for Step 2
    var examDate by remember { mutableStateOf("Select Date") }
    val calendar = Calendar.getInstance()

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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Progress Indicator
            LinearProgressIndicator(
                progress = { if (step == 1) 0.5f else 1f },
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            )

            if (step == 1) {
                // --- SCREEN 1: LICENSE SELECTION ---
                Text("Which license are you targeting?", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(24.dp))

                LicenseCard("Non-Professional", "For private vehicles", selectedLicense == "Non-Pro") {
                    selectedLicense = "Non-Pro"
                }
                Spacer(modifier = Modifier.height(12.dp))
                LicenseCard("Professional", "For for-hire/heavy vehicles", selectedLicense == "Pro") {
                    selectedLicense = "Pro"
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { step = 2 },
                    enabled = selectedLicense.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Next") }

            } else {
                // --- SCREEN 2: DATE SELECTION ---
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
                    onClick = { saveOnboardingData(selectedLicense, examDate, navController) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Start Learning") }
            }
        }
    }

}

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
            popUpTo("onboarding") { inclusive = true }
        }
    }
}