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
fun ExpresswaySigns(
    onPrevButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ) {
                Banner(context.getString(R.string.EXP_SEC))
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionText(
                content = listOf(context.getString(R.string.EXP_SEC_1))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Expressway Approach Signs
        item{
            SectionContent(
                title = context.getString(R.string.EXP_APP_SEC),
                content = listOf(context.getString(R.string.EXP_APP_SEC_1))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.sign_exp_app),
                contentDescription = "Expressway Approach Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Expressway Information Signs
        item{
            SectionTitle(
                title = context.getString(R.string.EXP_INF_SEC)
            )
            SectionContent(
                title = context.getString(R.string.EXP_INF_SEC_1),
                content = listOf(context.getString(R.string.EXP_INF_SEC_1_2))
            )
            Image(
                painter = painterResource(id = R.drawable.sign_exp_inf_1),
                contentDescription = "Expressway Information Signs 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            SectionContent(
                title = context.getString(R.string.EXP_INF_SEC_2),
                content = listOf(context.getString(R.string.EXP_INF_SEC_2_2))
            )
            Image(
                painter = painterResource(id = R.drawable.sign_exp_inf_2),
                contentDescription = "Expressway Information Signs 2",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            SectionContent(
                title = context.getString(R.string.EXP_INF_SEC_3),
                content = listOf(context.getString(R.string.EXP_INF_SEC_3_2))
            )
            Image(
                painter = painterResource(id = R.drawable.sign_exp_inf_3),
                contentDescription = "Expressway Information Signs 3",
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Advance Exit Signs
        item{
            SectionContent(
                title = context.getString(R.string.ADV_EXIT_SEC),
                content = listOf(context.getString(R.string.ADV_EXIT_SEC_1))
            )
            RSIRow(
                R.drawable.sign_adv_exit_1,
                R.drawable.sign_adv_exit_2,
                R.drawable.sign_adv_exit_3
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Previous and Next Button
        item{
            PrevButton(onClick = { onPrevButtonClicked() })
        }
    }
}