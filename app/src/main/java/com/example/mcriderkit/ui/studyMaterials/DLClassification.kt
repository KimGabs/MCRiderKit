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
fun DLClassification(
    onPrevButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Banner(context.getString(R.string.DLC_Banner))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            Image(
                painter = painterResource(id = R.drawable.dlc_image_1),
                contentDescription = "Tutorial Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            SectionContent(
                title = context.getString(R.string.DLC_SEC_1),
                content = listOf(
                    context.getString(R.string.DLC_1),
                    context.getString(R.string.DLC_2),
                    context.getString(R.string.DLC_3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            PrevNextButton(
                onPrevButtonClicked = { onPrevButtonClicked() },
                onNextButtonClicked = { onNextButtonClicked() }
            )
        }
    }
}