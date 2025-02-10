package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TrafficRulesAndRegulationsScreen() {
    LazyColumn(modifier = Modifier.padding(16.dp, 0.dp)) {
        item{
            Spacer(modifier = Modifier.height(8.dp))
            Banner("Traffic Rules & Regulations")
        }
        item {
            // Section for Speed Limits
            SectionContent(
                title = "Speed Limits",
                content = listOf(
                    "Always follow the posted speed limit signs.",
                    "Speed limits are typically set based on road conditions, traffic volume, and safety concerns.",
                    "Exceeding the speed limit can result in fines or other penalties."
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            // Section for Right of Way
            SectionContent(
                title = "Right of Way",
                content = listOf(
                    "At intersections, vehicles must yield to traffic coming from the right unless otherwise posted.",
                    "Pedestrians always have the right of way at pedestrian crossings.",
                    "In roundabouts, vehicles inside the circle have the right of way."
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            // Section for Parking Regulations
            SectionContent(
                title = "Parking Regulations",
                content = listOf(
                    "Do not park in spaces reserved for people with disabilities unless you have the proper permit.",
                    "Avoid parking in areas marked with a red or yellow curb.",
                    "Ensure your vehicle is parked within the designated parking lines to avoid fines."
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            // Section for Other Key Laws
            SectionContent(
                title = "Other Key Laws",
                content = listOf(
                    "Always wear your seatbelt while driving or riding as a passenger.",
                    "Do not use a mobile phone while driving unless you have a hands-free device.",
                    "Driving under the influence of alcohol or drugs is strictly prohibited."
                )
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun SectionContent(title: String, content: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        content.forEach { item ->
            Text(
                text = "• $item",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun SectionText(content: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        content.forEach { item ->
            Text(
                text = "• $item",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Composable
fun SubsectionContent(title: String, content: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        content.forEach { item ->
            Text(
                text = "• $item",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                textAlign = TextAlign.Justify
                )
        }
    }
}

@Composable
fun SectionTitle(title: String){
    Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun Banner(textBanner: String) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
        .padding(0.dp, 8.dp)
        .background(Color(0xFF4CAF50))) {  // You can customize the color here
        Text(
            text = textBanner,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
            lineHeight = 30.sp
        )
    }
}
