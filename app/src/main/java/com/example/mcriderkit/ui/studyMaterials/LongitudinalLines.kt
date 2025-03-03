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
fun LongitudinalLines(
    onNextButtonClicked: () -> Unit,
) {
    val context = LocalContext.current

    LazyColumn() {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ){
                Banner(context.getString(R.string.LONG_MARK_SEC))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_1),
                content = listOf(context.getString(R.string.LONG_MARK_SEC_1_2))
            )
            Image(
                painter = painterResource(id = R.drawable.mark_center_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_1),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_2),
                content = listOf(context.getString(R.string.LONG_MARK_SEC_2_2))
            )
            Image(
                painter = painterResource(id = R.drawable.mark_lane_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionTitle(context.getString(R.string.LONG_MARK_SEC_3))
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_3_1),
                content = listOf(
                    context.getString(R.string.LONG_MARK_SEC_3_1_2),
                )
            )
            Image(
                painter = painterResource(id = R.drawable.mark_double_solid_yellow_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_3_2),
                content = listOf(context.getString(R.string.LONG_MARK_SEC_3_2_2))
            )
            Image(
                painter = painterResource(id = R.drawable.mark_yellow_center_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_3_2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_3_3),
                content = listOf(
                    context.getString(R.string.LONG_MARK_SEC_3_3_2),
                    context.getString(R.string.LONG_MARK_SEC_3_3_3)

                )
            )
            Image(
                painter = painterResource(id = R.drawable.mark_yellow_center_line_broken_white_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_3_3),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_3_4),
                content = listOf(context.getString(R.string.LONG_MARK_SEC_3_4_2))
            )
            Image(
                painter = painterResource(id = R.drawable.mark_broken_yellow_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_3_4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_3_5),
                content = listOf(context.getString(R.string.LONG_MARK_SEC_3_5_2))
            )
            Image(
                painter = painterResource(id = R.drawable.mark_broken_solid_yellow_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_3_5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_4),
                content = listOf(context.getString(R.string.LONG_MARK_SEC_4_2))
            )
            Image(
                painter = painterResource(id = R.drawable.mark_edge_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_4),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LONG_MARK_SEC_5),
                content = listOf(
                    context.getString(R.string.LONG_MARK_SEC_5_2),
                    context.getString(R.string.LONG_MARK_SEC_5_3)
                )
            )
            Image(
                painter = painterResource(id = R.drawable.mark_continuity_line),
                contentDescription = context.getString(R.string.LONG_MARK_SEC_5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            NextButton(onNextButtonClicked)
        }
    }
}