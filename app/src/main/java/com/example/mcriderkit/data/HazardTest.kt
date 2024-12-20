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
    val lastScore: Int = 0               // User's last score
)
