package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun GeneralProcedures() {
    val context = LocalContext.current

    LazyColumn(modifier = Modifier.padding(16.dp, 0.dp)) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Banner(context.getString(R.string.GP_SEC_1))
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
    }
}