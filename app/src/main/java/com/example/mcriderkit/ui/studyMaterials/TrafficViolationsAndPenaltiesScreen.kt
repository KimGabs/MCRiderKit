package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.mcriderkit.R

@Composable
fun TrafficViolationsAndPenaltiesScreen() {
    val context = LocalContext.current

    LazyColumn() {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ){
                Banner(context.getString(R.string.TVP_SEC))
            }
            Spacer(modifier = Modifier.height(16.dp))

        }
        item {
            // Section for Driving Without a Valid License
            SectionContent(
                title = context.getString(R.string.driving_without_license_title),
                content = listOf(
                    context.getString(R.string.driving_without_license_desc),
                    context.getString(R.string.driving_without_license_penalty)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Reckless Driving
            SectionContent(
                title = context.getString(R.string.reckless_driving_title),
                content = listOf(
                    context.getString(R.string.reckless_driving_1st),
                    context.getString(R.string.reckless_driving_2nd),
                    context.getString(R.string.reckless_driving_3rd),
                    context.getString(R.string.reckless_driving_subsequent)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Driving Under the Influence (DUI)
            SectionContent(
                title = context.getString(R.string.dui_title),
                content = listOf(
                    context.getString(R.string.dui_non_professional),
                    context.getString(R.string.dui_non_professional_1st),
                    context.getString(R.string.dui_non_professional_2nd),
                    context.getString(R.string.dui_professional),
                    context.getString(R.string.dui_professional_1st)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Failure to Wear Seatbelt
            SectionContent(
                title = context.getString(R.string.seatbelt_title),
                content = listOf(
                    context.getString(R.string.seatbelt_1st),
                    context.getString(R.string.seatbelt_2nd),
                    context.getString(R.string.seatbelt_3rd)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Failure to Wear Standard Protective Motorcycle Helmet
            SectionContent(
                title = context.getString(R.string.helmet_title),
                content = listOf(
                    context.getString(R.string.helmet_1st),
                    context.getString(R.string.helmet_2nd),
                    context.getString(R.string.helmet_3rd),
                    context.getString(R.string.helmet_subsequent)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Illegal Parking
            SectionContent(
                title = context.getString(R.string.illegal_parking_title),
                content = listOf(
                    context.getString(R.string.illegal_parking_desc),
                    context.getString(R.string.illegal_parking_penalty)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Disregarding Traffic Signs
            SectionContent(
                title = context.getString(R.string.disregarding_signs_title),
                content = listOf(
                    context.getString(R.string.disregarding_signs_desc),
                    context.getString(R.string.disregarding_signs_penalty)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Driving an Unregistered Motor Vehicle
            SectionContent(
                title = context.getString(R.string.unregistered_vehicle_title),
                content = listOf(
                    context.getString(R.string.unregistered_vehicle_desc),
                    context.getString(R.string.unregistered_vehicle_penalty)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            // Section for Unauthorized Modification of Motor Vehicle
            SectionContent(
                title = context.getString(R.string.unauthorized_modification_title),
                content = listOf(
                    context.getString(R.string.unauthorized_modification_desc),
                    context.getString(R.string.unauthorized_modification_penalty)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
