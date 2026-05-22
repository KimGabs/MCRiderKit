package com.example.mcriderkit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * A beautiful, full-screen UI Guide overlay that displays your exported Canva slides.
 * Now supports an optional "Don't show this again" local persistence checkbox.
 *
 * @param slideImages A list of drawable resource IDs to display (e.g., listOf(R.drawable.slide_1, ...))
 * @param finishButtonText The label on the CTA button on the last slide (e.g., "Get Started" or "Done")
 * @param showDontShowAgain Whether to display the "Don't show this again" checkbox
 * @param dontShowAgainChecked Current state of the persistent checkbox
 * @param onDontShowAgainChanged Callback triggered when user toggles the checkbox
 * @param onGuideComplete Callback triggered when the user skips or reaches the end of the guide
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CanvaUiGuideOverlay(
    slideImages: List<Int>,
    finishButtonText: String = "Get Started",
    showDontShowAgain: Boolean = false,
    dontShowAgainChecked: Boolean = false,
    onDontShowAgainChanged: (Boolean) -> Unit = {},
    onGuideComplete: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { slideImages.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f)) // Dark backdrop to focus on the slides
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            // Container Card for the Canva Slide
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Takes up remaining vertical space
            ) {
                Box(modifier = Modifier.fillMaxSize()) {

                    // Swipeable horizontal carousel displaying your images
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Image(
                            painter = painterResource(id = slideImages[page]),
                            contentDescription = "Tutorial Step ${page + 1}",
                            contentScale = ContentScale.Fit, // Keeps original Canva slide proportions intact
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))


            // Footer controls: Dot indicators & Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (showDontShowAgain) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onDontShowAgainChanged(!dontShowAgainChecked) }
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Checkbox(
                            checked = dontShowAgainChecked,
                            onCheckedChange = onDontShowAgainChanged,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = Color.White.copy(alpha = 0.6f),
                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Don't show this again",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }
                // 2. Control Action Buttons (Skip & Next/Finish)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Show "Skip" button if not on the final slide
                    if (pagerState.currentPage < slideImages.size - 1) {
                        TextButton(onClick = onGuideComplete) {
                            Text("Skip", color = Color.White.copy(alpha = 0.7f))
                        }
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage < slideImages.size - 1) {
                                // Transition to next slide
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                // Finish tutorial
                                onGuideComplete()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = if (pagerState.currentPage == slideImages.size - 1) finishButtonText else "Next"
                        )
                    }
                }
            }
        }
    }
}