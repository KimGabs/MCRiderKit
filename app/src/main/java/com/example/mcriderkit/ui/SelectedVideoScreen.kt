package com.example.mcriderkit.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mcriderkit.data.HazardTest

@Composable
fun SelectedVideoScreen(
    video: HazardTest, // The selected video object
    onStartTestClicked: () -> Unit, // Callback when user clicks "Start Test"
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Display the thumbnail of the selected video
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f) // Keeping the aspect ratio of the video thumbnail
                .clip(RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource(id = video.thumbnailId),
                contentDescription = "Thumbnail for ${video.title}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display the video title
        Text(
            text = video.title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        // Display video details
        Text(
            text = "Last Score: ${video.lastScore}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Location: ${video.location}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Video Length: ${video.videoLength} Seconds",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Speed Limit: ${video.speedLimit}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Time of Day: ${video.timeOfDay}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Weather: ${video.weather}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Button to start the hazard perception test
        Button(
            onClick = onStartTestClicked,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Start Hazard Perception Test", style = MaterialTheme.typography.titleMedium)
        }
    }
}
