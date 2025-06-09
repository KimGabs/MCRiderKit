package com.example.mcriderkit.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.example.mcriderkit.data.DataSource.TutorialPage
import com.example.mcriderkit.data.DataSource.tutorialPages

@Composable
fun TutorialScreen(
    tutorialType: String,
    onFinishTutorial: () -> Unit
) {
    val pages = getPagesByType(tutorialType)
    var currentPage = remember { mutableStateOf(0) }

    if (pages.isEmpty()) {
        // Handle empty list scenario (no pages for this type)
        Text("No tutorial pages available.")
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyColumn() {
            item {
                // Image placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                ) {
                    Image(
                        painter = painterResource(id = pages[currentPage.value].image),
                        contentDescription = "Tutorial Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                }

                // Page text
                Text(
                    text = pages[currentPage.value].text,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )

                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (currentPage.value > 0) currentPage.value-- },
                        enabled = currentPage.value > 0
                    ) {
                        Text("Previous")
                    }

                    if (currentPage.value < pages.size - 1) {
                        Button(
                            onClick = { currentPage.value++ }
                        ) {
                            Text("Next")
                        }
                    } else {
                        Button(
                            onClick = onFinishTutorial
                        ) {
                            Text("Finish")
                        }
                    }
                }
            }}
        }


}

fun getPagesByType(title: String): List<TutorialPage> {
    return tutorialPages.filter { it.title == title }
}