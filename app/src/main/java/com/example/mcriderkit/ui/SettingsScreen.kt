package com.example.mcriderkit.ui

import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
//    onLanguageChange: (String) -> Unit,
) {
    val appVersion = "1.0.0" // Replace with your app's version"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Change Language Section
        Text(text = "Change Language", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        // Language selection drop-down
//        val languages = listOf("English", "Filipino", "Spanish") // Example languages

        var currentLanguage = "English" // Replace with your default language
        // Language selection drop-down
        var expanded by remember { mutableStateOf(false) }
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }, // Handle the click event on the entire row
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between Text and Icon
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {
                Text(
                    text = currentLanguage,
                    style = MaterialTheme.typography.bodyMedium, // Customize the text style
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f) // Let the text take up the available space
                )
                IconButton(
                    onClick = { expanded = !expanded }, // Toggle dropdown when icon is clicked
                    modifier = Modifier.size(24.dp) // Adjust the size of the icon button
                ) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }


            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                val languages = listOf("English", "Filipino") // Example languages
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(text = language) },
                        onClick = {}
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display App Version
        Text(
            text = "App Version: $appVersion",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

// Function to get app version
private fun getAppVersion(context: Context): String {
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return packageInfo.versionName.toString()
}
