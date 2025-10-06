package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun EmergencyScreen(
    onPrevButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn()
    {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Banner(context.getString(R.string.Emergency_banner))
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(context.getString(R.string.Emergency_brake))
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.emegency_brake),
                contentDescription = "Attitude and Behavior 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionContent(
                title = context.getString(R.string.Emergency_brake_point1),
                content = listOf(
                    context.getString(R.string.Emergency_brake_point1_1),
                    context.getString(R.string.Emergency_brake_point1_2),
                    context.getString(R.string.Emergency_brake_point1_3),
                    context.getString(R.string.Emergency_brake_point1_4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionTitle(context.getString(R.string.Skidding))
            Spacer(modifier = Modifier.height(8.dp))
            SectionText(content = listOf(context.getString(R.string.Skidding_point1)))
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.skidding),
                contentDescription = "Attitude and Behavior 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

        }
        item {
            SectionContent(
                title = context.getString(R.string.Skidding_point2),
                content = listOf(
                    context.getString(R.string.Skidding_point2_1),
                    context.getString(R.string.Skidding_point2_2),
                    context.getString(R.string.Skidding_point2_3),
                    context.getString(R.string.Skidding_point2_4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            SectionTitle(context.getString(R.string.Collision_title))
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.intersection_visual),
                contentDescription = "Attitude and Behavior 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            SectionText(content = listOf(context.getString(R.string.Collision_scenario)))
            Spacer(modifier = Modifier.height(8.dp))
            SectionText(content = listOf(context.getString(R.string.Collision_question)))
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            SectionText(content = listOf(context.getString(R.string.Collision_intro_point)))
            Spacer(modifier = Modifier.height(16.dp))
            SectionContent(
                title = context.getString(R.string.Collision_point1),
                content = listOf(
                    context.getString(R.string.Collision_point1_1)
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
        item{
            SectionContent(
                title = context.getString(R.string.Collision_point2),
                content = listOf(
                    context.getString(R.string.Collision_point2_1),
                    context.getString(R.string.Collision_point2_2),
                    context.getString(R.string.Collision_point2_3),
                    context.getString(R.string.Collision_point2_4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            SectionContent(
                title = context.getString(R.string.Collision_point3),
                content = listOf(
                    context.getString(R.string.Collision_point3_1),
                    context.getString(R.string.Collision_point3_2)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            SectionContent(
                title = context.getString(R.string.Collision_point4),
                content = listOf(
                    context.getString(R.string.Collision_point4_1),
                    context.getString(R.string.Collision_point4_2),
                    context.getString(R.string.Collision_point4_3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

        }

        item{
            PrevNextButton(
                onPrevButtonClicked = onPrevButtonClicked,
                onNextButtonClicked = onNextButtonClicked
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}