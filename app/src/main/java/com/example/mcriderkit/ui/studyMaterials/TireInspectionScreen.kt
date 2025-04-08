package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.mcriderkit.R

@Composable
fun TireInspectionScreen(
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ) {
                Banner(context.getString(R.string.tire_inspection_banner_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Visual Inspection
            SectionContent(
                title = context.getString(R.string.visual_inspection_title),
                content = listOf(
                    context.getString(R.string.visual_inspection_point1),
                    context.getString(R.string.visual_inspection_point2),
                    context.getString(R.string.visual_inspection_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Tread Depth
            SectionContent(
                title = context.getString(R.string.tread_depth_point1),
                content = listOf(
                    context.getString(R.string.tread_depth_point1),
                    context.getString(R.string.tread_depth_point2),
                    context.getString(R.string.tread_depth_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Tire Pressure
            SectionContent(
                title = context.getString(R.string.tire_pressure_title),
                content = listOf(
                    context.getString(R.string.tire_pressure_point1),
                    context.getString(R.string.tire_pressure_point2),
                    context.getString(R.string.tire_pressure_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Tire Age
            SectionContent(
                title = context.getString(R.string.tire_age_title),
                content = listOf(
                    context.getString(R.string.tire_age_point1),
                    context.getString(R.string.tire_age_point2),
                    context.getString(R.string.tire_age_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Spare Tire
            SectionContent(
                title = context.getString(R.string.spare_tire_title),
                content = listOf(
                    context.getString(R.string.spare_tire_point1),
                    context.getString(R.string.spare_tire_point2),
                    context.getString(R.string.spare_tire_point3)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item{
            NextButton(onNextButtonClicked)
        }
    }
}
