package com.example.mcriderkit.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserInputScreen(
    onSubmit: (String) -> Unit,
    context: Context // Pass context to access SharedPreferences
) {
    var name by remember { mutableStateOf("") }
    var isNameValid by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Enter your name", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Name input field
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                isNameValid = it.isNotEmpty()
            },
            label = { Text("Name") },
            isError = !isNameValid,
            modifier = Modifier.fillMaxWidth(),
        )

        if (!isNameValid) {
            Text("Name cannot be empty", color = Color.Red, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isNameValid) {
                    // Save name in SharedPreferences
                    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putString("user_name", name).apply()

                    // Navigate to main app
                    onSubmit(name)
                }
            },
            enabled = isNameValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}