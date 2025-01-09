package com.example.mcriderkit.ui.components

import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.data.HazardTestDao
import com.example.mcriderkit.R

class HazardRepository(private val dao: HazardTestDao) {

    private val presetHazardTests = listOf(
        HazardTest(
            title = "Test Video 1",
            location = "Location 1",
            speedLimit = "60 km/h",
            timeOfDay = "Morning",
            weather = "Clear",
            lastScore = 85,
            videoPath = "test1",
            thumbnailId = R.drawable.thumbnail1,
            videoLength = 12,
            hazardTime = 8
        ),
        HazardTest(
            title = "Test Video 2",
            location = "Location 2",
            speedLimit = "40 km/h",
            timeOfDay = "Afternoon",
            weather = "Clear",
            lastScore = 10,
            videoPath = "test2",
            thumbnailId = R.drawable.thumbnail2,
            videoLength = 19,
            hazardTime = 6
        )

    )

    suspend fun insertPresetData() {
        val existingTests = dao.getAllHazardTests()
        if (existingTests.isEmpty()) {
            dao.insertAll(presetHazardTests)
        }
    }

    suspend fun getAllHazardTests() = dao.getAllHazardTests()
    suspend fun getHazardTestById(id: Int) = dao.getHazardTestById(id)
    suspend fun updateScore(hazardTest: HazardTest) = dao.updateScore(hazardTest)
}