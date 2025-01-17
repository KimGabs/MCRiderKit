package com.example.mcriderkit.ui

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TutorialScreen(
    onNextClicked: () -> Unit

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
//        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp) // Adjust height as needed
                .background(Color.LightGray), // Placeholder color
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Image Placeholder",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Text Content
        Text(
            text = "This is where you explain the purpose or instructions of the tutorial. Add concise and clear details here.",
            color = Color.Black,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(vertical = 16.dp),
        )

        Spacer(modifier = Modifier.height(26.dp))
        // Next Button
        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TutorialScreenPreview() {
    TutorialScreen(onNextClicked = {})
}
