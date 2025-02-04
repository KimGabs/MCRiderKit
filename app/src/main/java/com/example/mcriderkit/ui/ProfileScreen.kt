package com.example.mcriderkit.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mcriderkit.R
import com.example.mcriderkit.data.QuizScore


@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val userName = sharedPref.getString("user_name", "Rider") ?: "Rider" // Default name

    LazyColumn (
        modifier = Modifier.padding(16.dp, 8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item{
            Text(text = "Welcome, $userName!", style = MaterialTheme.typography.headlineMedium)
        }
    }
}


