package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun WarningSign(
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
                Banner(context.getString(R.string.REG_SEC_1))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            RoadSignItem(R.drawable.sign_stop)
            SectionContent(
                title = context.getString(R.string.STOP_SIGN),
                content = listOf(context.getString(R.string.STOP_SIGN_1))
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            NextButton(onNextButtonClicked)
        }
    }
}