package com.example.mcriderkit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage

@Composable
fun QuizScreen(navController: NavHostController) {
    Text(text = "Quiz Screen")
    val imageUrl = "https://www.clipartmax.com/png/middle/280-2807232_goku-black-super-saiyan-rose-by-frost-z-dbc5q4k-goku-black-super.png"
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1280f/1280f)
        )
    }
}