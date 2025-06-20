package com.example.mcriderkit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hazard_tests")
data class HazardTest(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val videoPath: String,               // Path or URL to the video
    val location: String,                // Location of the test
    val speedLimit: String,              // Speed limit in the clip
    val timeOfDay: String,               // Morning/Evening/Night
    val weather: String,                 // Weather conditions
    var lastScore: Int = 0,            // User's last score
    val thumbnailId: Int,            // URL or path to the thumbnail image
    val videoLength: Int,               // Length of the video
    val earlyRange: Double,              // Start of hazard
    val perfectRange: Double,                // Perfect timing range
    val goodRange: Double,                 // Good timing range
    val lateRange: Double,                // End of hazard
    var trophy: Boolean = false
)
