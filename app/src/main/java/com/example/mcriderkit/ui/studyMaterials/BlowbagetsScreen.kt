package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.mcriderkit.R

@Composable
fun BlowbagetsChecklistScreen(
    onPrevButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Banner(textBanner = context.getString(R.string.blowbagets_banner_title))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.blowbagets),
                contentDescription = "Attitude and Behavior 1",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Battery Section
            SectionContent(
                title = context.getString(R.string.battery_title),
                content = listOf(
                    context.getString(R.string.battery_point1),
                    context.getString(R.string.battery_point2),
                    context.getString(R.string.battery_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Lights Section
            SectionContent(
                title = context.getString(R.string.lights_title),
                content = listOf(
                    context.getString(R.string.lights_point1),
                    context.getString(R.string.lights_point2),
                    context.getString(R.string.lights_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Oil Section
            SectionContent(
                title = context.getString(R.string.oil_title),
                content = listOf(
                    context.getString(R.string.oil_point1),
                    context.getString(R.string.oil_point2),
                    context.getString(R.string.oil_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Water Section
            SectionContent(
                title = context.getString(R.string.water_title),
                content = listOf(
                    context.getString(R.string.water_point1),
                    context.getString(R.string.water_point2),
                    context.getString(R.string.water_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Brakes Section
            SectionContent(
                title = context.getString(R.string.brakes_title),
                content = listOf(
                    context.getString(R.string.brakes_point1),
                    context.getString(R.string.brakes_point2),
                    context.getString(R.string.brakes_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Air Section
            SectionContent(
                title = context.getString(R.string.air_title),
                content = listOf(
                    context.getString(R.string.air_point1),
                    context.getString(R.string.air_point2),
                    context.getString(R.string.air_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Gas Section
            SectionContent(
                title = context.getString(R.string.gas_title),
                content = listOf(
                    context.getString(R.string.gas_point1),
                    context.getString(R.string.gas_point2),
                    context.getString(R.string.gas_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Engine Section
            SectionContent(
                title = context.getString(R.string.engine_title),
                content = listOf(
                    context.getString(R.string.engine_point1),
                    context.getString(R.string.engine_point2),
                    context.getString(R.string.engine_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Tire Section
            SectionContent(
                title = context.getString(R.string.tires_title),
                content = listOf(
                    context.getString(R.string.tires_point1),
                    context.getString(R.string.tires_point2),
                    context.getString(R.string.tires_point3)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Self Section
            SectionContent(
                title = context.getString(R.string.self_title),
                content = listOf(
                    context.getString(R.string.self_point1),
                    context.getString(R.string.self_point2),
                    context.getString(R.string.self_point3)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        // Previous and Next Button
        item{
            PrevButton(onPrevButtonClicked)
        }
    }
}