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
fun OtherLines(
    onPrevButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ){
                Banner(context.getString(R.string.OTHER_LINES_SEC))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.OTHER_LINES_SEC_2),
                content = listOf(context.getString(R.string.OTHER_LINES_SEC_2_2))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.mark_turn_line),
                contentDescription = context.getString(R.string.OTHER_LINES_SEC_2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.OTHER_LINES_SEC_3),
                content = listOf(context.getString(R.string.OTHER_LINES_SEC_3_2))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.mark_parking_bay),
                contentDescription = context.getString(R.string.OTHER_LINES_SEC_3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Previous and Next Button
        item{
            PrevButton(
                onClick = { onPrevButtonClicked() }
            )
        }
    }
}