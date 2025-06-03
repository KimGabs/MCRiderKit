package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun PermitsLicenses(
    onNextButtonClicked: () -> Unit,
) {
    val context = LocalContext.current

    LazyColumn() {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Banner(context.getString(R.string.PAL_BANNER))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Student-Driver's Permit
            SectionContent(
                title = context.getString(R.string.PAL_SEC_1),
                content = listOf(context.getString(R.string.PAL_1), context.getString(R.string.PAL_1_2))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            // Section for Validity of Student-Driver’s Permit
            SectionTitle(
                title = context.getString(R.string.PAL_SEC_2)
            )

            Spacer(modifier = Modifier.height(12.dp))

            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_2_1),
                content = listOf(context.getString(R.string.PAL_2_1_1), context.getString(R.string.PAL_2_1_2))

            )

            Spacer(modifier = Modifier.height(16.dp))

            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_2_2),
                content = listOf(context.getString(R.string.PAL_2_2_1), context.getString(R.string.PAL_2_2_2))
            )

            Spacer(modifier = Modifier.height(16.dp))

            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_2_3),
                content = listOf(context.getString(R.string.PAL_2_3_1), context.getString(R.string.PAL_2_3_2))
            )
            Spacer(modifier = Modifier.height(22.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.PAL_SEC_5),
                content = listOf(context.getString(R.string.PAL_5))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_6),
                content = listOf(context.getString(R.string.PAL_6))
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_7),
                content = listOf(context.getString(R.string.PAL_7))
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_8),
                content = listOf(context.getString(R.string.PAL_8))
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_9),
                content = listOf(context.getString(R.string.PAL_9))
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_10),
                content = listOf(context.getString(R.string.PAL_10))
            )
            Spacer(modifier = Modifier.height(16.dp))
            SubsectionContent(
                title = context.getString(R.string.PAL_SEC_11),
                content = listOf(context.getString(R.string.PAL_11))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            NextButton(onClick = { onNextButtonClicked() })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier // Allow external modifiers
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(16.dp, 0.dp)
    ) {
        Text("Next")
    }
}

@Composable
fun PrevButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().padding(16.dp, 0.dp)
    ) {
        Text("Prev")
    }
}

@Composable
fun PrevNextButton(
    onPrevButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Adds spacing between buttons
    ) {
        PrevButton(
            onClick = onPrevButtonClicked,
            modifier = Modifier.weight(1f) // Ensures equal width
        )
        NextButton(
            onClick = onNextButtonClicked,
            modifier = Modifier.weight(1f) // Ensures equal width
        )
    }
}