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
fun GuideSigns(
    onPrevButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Banner(context.getString(R.string.GUIDE_SEC))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Advance Direction Signs
        item{
            SectionContent(
                title = context.getString(R.string.ADV_DIR_SEC),
                content = listOf(context.getString(R.string.ADV_DIR_SEC_1))
            )
            Image(
                painter = painterResource(id = R.drawable.sign_advance_directions),
                contentDescription = "Advance Direction Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Intersection Direction Signs
        item{
            SectionContent(
                title = context.getString(R.string.INT_DIR_SEC),
                content = listOf(context.getString(R.string.INT_DIR_SEC_1))
            )
            Image(
                painter = painterResource(id = R.drawable.sign_intersection_direction),
                contentDescription = "Intersection Direction Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Reassurance Direction Signs
        item{
            SectionContent(
                title = context.getString(R.string.REAS_DIR_SEC),
                content = listOf(context.getString(R.string.REAS_DIR_SEC_1))
            )
            Image(
                painter = painterResource(id = R.drawable.sign_reassurance_direction),
                contentDescription = "Reassurance Direction Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
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