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
fun DefensiveDrivingScreen(
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
                Banner(context.getString(R.string.DEF_Banner))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SectionText(content = listOf(context.getString(R.string.DEF_point1)))
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Image(
                painter = painterResource(id = R.drawable.defensive_driving_tips),
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
                title = context.getString(R.string.DEF_Title1),
                content = listOf(
                    context.getString(R.string.DEF_point2_1),
                    context.getString(R.string.DEF_point2_2),
                    context.getString(R.string.DEF_point2_3),
                    context.getString(R.string.DEF_point2_4),
                    context.getString(R.string.DEF_point2_5),
                    context.getString(R.string.DEF_point2_6),
                    context.getString(R.string.DEF_point2_7),
                    context.getString(R.string.DEF_point2_8),
                    context.getString(R.string.DEF_point2_9)
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