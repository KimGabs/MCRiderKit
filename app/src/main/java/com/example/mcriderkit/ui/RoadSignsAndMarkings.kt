package com.example.mcriderkit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R


@Composable
fun RoadSignsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Banner("Road Signs and Markings")
        }

        item {
            SectionTitle("Regulatory Signs")
            RoadSignItem(title = "Stop Sign", imageRes = R.drawable.sign_stop)
            RoadSignItem(title = "Yield Sign", imageRes = R.drawable.sign_give_way)
            RoadSignItem(title = "Speed Limit Sign", imageRes = R.drawable.sign_speed_limit)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Warning Signs")
            RoadSignItem(title = "Pedestrian Crossing", imageRes = R.drawable.sign_pedestrian_crossing)
            RoadSignItem(title = "Reverse Turn", imageRes = R.drawable.sign_reverse_turn)
            RoadSignItem(title = "Slippery Road", imageRes = R.drawable.sign_steep_descent)
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle("Informational Signs")
            RoadSignItem(title = "Directional Guide", imageRes = R.drawable.sign_directional_guide)
            RoadSignItem(title = "Service Sign", imageRes = R.drawable.sign_service_sign)
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun RoadSignItem(title: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "$title image",
            modifier = Modifier
                .size(80.dp)
                .padding(end = 14.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}