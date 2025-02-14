package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun Qualifications(
    onPrevButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ){
                Banner(context.getString(R.string.QDR_Banner))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionTitle(context.getString(R.string.QDR_SEC_1))
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Image(
                painter = painterResource(id = R.drawable.qdr_image_1),
                contentDescription = "Driving Indications",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.9f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SectionContent(
                title = context.getString(R.string.QDR_SEC_2),
                content = listOf(
                    context.getString(R.string.QDR_1),
                    context.getString(R.string.QDR_2)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SectionTitle(
                title = context.getString(R.string.QDR_SEC_3),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.qdr_image_2),
                contentDescription = "Student-Driver Permit Requirements",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )
            Image(
                painter = painterResource(id = R.drawable.qdr_image_3),
                contentDescription = "Student-Driver Permit Requirements",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.9f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionTitle(context.getString(R.string.QDR_SEC_4))
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.qdr_image_4),
                contentDescription = "New Driver License Requirements",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            SectionText(
                content = listOf(context.getString(R.string.QDR_3))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionTitle(context.getString(R.string.QDR_SEC_5))
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.qdr_image_5),
                contentDescription = "New Driver License Requirements",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            SectionContent(
                title = context.getString(R.string.QDR_SEC_6),
                content = listOf(
                    context.getString(R.string.QDR_4)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

//      Previous and Next Button
        item {
            PrevNextButton(
                onPrevButtonClicked = { onPrevButtonClicked() },
                onNextButtonClicked = { onNextButtonClicked() }
            )
        }
    }

}