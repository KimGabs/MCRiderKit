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
fun GeneralProcedures(
    modifier: Modifier = Modifier,
    onPrevButtonClicked: () -> Unit,
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Banner(context.getString(R.string.GP_SEC_1))
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            Image(
                painter = painterResource(id = R.drawable.gp_image_1),
                contentDescription = "General Procedures",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.8f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            PrevButton(onClick = { onPrevButtonClicked() })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}