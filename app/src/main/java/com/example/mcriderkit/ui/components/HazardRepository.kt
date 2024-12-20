package com.example.mcriderkit.ui.components

import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.data.HazardTestDao

class HazardRepository(private val dao: HazardTestDao) {

    private val presetHazardTests = listOf(
        HazardTest(title = "Test Video 1", location = "Location 1", speedLimit = "60 km/h", timeOfDay = "Morning", weather = "Clear", lastScore = 85, videoPath = "video1.mp4")
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