package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun EngineOilInspectionScreen(
    onNextButtonClicked: () -> Unit,
    onPrevButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ) {
                Banner(textBanner = context.getString(R.string.engine_oil_inspection_banner_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Importance of Engine Oil
            SectionContent(
                title = context.getString(R.string.importance_of_engine_oil_title),
                content = listOf(
                    context.getString(R.string.importance_of_engine_oil_point1),
                    context.getString(R.string.importance_of_engine_oil_point2),
                    context.getString(R.string.importance_of_engine_oil_point3),
                    context.getString(R.string.importance_of_engine_oil_point4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Checking Oil Level
            SectionContent(
                title = context.getString(R.string.checking_oil_level_title),
                content = listOf(
                    context.getString(R.string.checking_oil_level_point1),
                    context.getString(R.string.checking_oil_level_point2),
                    context.getString(R.string.checking_oil_level_point3),
                    context.getString(R.string.checking_oil_level_point4),
                    context.getString(R.string.checking_oil_level_point5)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Assessing Oil Condition
            SectionContent(
                title = context.getString(R.string.assessing_oil_condition_title),
                content = listOf(
                    context.getString(R.string.assessing_oil_condition_point1),
                    context.getString(R.string.assessing_oil_condition_point2),
                    context.getString(R.string.assessing_oil_condition_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Oil Change Intervals
            SectionContent(
                title = context.getString(R.string.oil_change_intervals_title),
                content = listOf(
                    context.getString(R.string.oil_change_intervals_point1),
                    context.getString(R.string.oil_change_intervals_point2),
                    context.getString(R.string.oil_change_intervals_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Previous and Next Button
        item{
            PrevNextButton(
                onPrevButtonClicked = onPrevButtonClicked,
                onNextButtonClicked = onNextButtonClicked
            )
        }
    }
}