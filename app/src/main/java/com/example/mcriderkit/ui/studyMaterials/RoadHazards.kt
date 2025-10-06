package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun RoadHazardsScreen(
    onPrevButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Banner(textBanner = context.getString(R.string.RoadHazards_banner))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Hazards Section
            SectionContent(
                title = context.getString(R.string.RH_title),
                content = listOf(
                    context.getString(R.string.RH_point1),
                    context.getString(R.string.RH_point2)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionContent(
                title = context.getString(R.string.RH_title2),
                content = listOf(
                    context.getString(R.string.RH_point3),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            SectionContent(
                title = context.getString(R.string.RH_title3),
                content = listOf(
                    context.getString(R.string.RH_point4),
                )
            )

        }
        item{
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.rh_image_1),
                contentDescription = "Road Hazards",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
        }

        item{
            SectionContent(
                title = "",
                content = listOf(
                    context.getString(R.string.RH_point5),
                    context.getString(R.string.RH_point6),
                    context.getString(R.string.RH_point7)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Previous and Next Button
        item{
            PrevButton(onPrevButtonClicked)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}