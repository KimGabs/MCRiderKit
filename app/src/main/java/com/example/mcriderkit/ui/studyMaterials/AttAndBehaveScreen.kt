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
fun AttAndBehaveScreen(
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
                Banner(context.getString(R.string.AB_Banner))
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(context.getString(R.string.AB_title1))
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionText(content = listOf(context.getString(R.string.AB_point1)))
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.ab_visual1),
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
                title = context.getString(R.string.AB_point2),
                content = listOf(
                    context.getString(R.string.AB_point2_1),
                    context.getString(R.string.AB_point2_2),
                    context.getString(R.string.AB_point2_3),
                    context.getString(R.string.AB_point2_4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionContent(
                title = context.getString(R.string.AB_point3),
                content = listOf(
                    context.getString(R.string.AB_point3_1),
                    context.getString(R.string.AB_point3_2),
                    context.getString(R.string.AB_point3_3),
                    context.getString(R.string.AB_point3_4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.ab_visual2),
                contentDescription = "Attitude and Behavior 2",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            SectionContent(
                title = context.getString(R.string.AB_point4),
                content = listOf(
                    context.getString(R.string.AB_point4_1),
                    context.getString(R.string.AB_point4_2)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionTitle(context.getString(R.string.AB_title2))
            Spacer(modifier = Modifier.height(8.dp))
            SectionText(content = listOf(context.getString(R.string.AB_point5)))
            Spacer(modifier = Modifier.height(16.dp))
            SectionText(content = listOf(context.getString(R.string.AB_point6)))
            Spacer(modifier = Modifier.height(16.dp))

        }

        item{
            Image(
                painter = painterResource(id = R.drawable.ab_visual3),
                contentDescription = "Attitude and Behavior 2",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            SectionContent(
                title = context.getString(R.string.AB_point7),
                content = listOf(
                    context.getString(R.string.AB_point7_1),
                    context.getString(R.string.AB_point7_2),
                    context.getString(R.string.AB_point7_3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            NextButton(onNextButtonClicked)
            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}