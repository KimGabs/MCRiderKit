package com.example.mcriderkit.ui.components

import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.data.HazardTestDao
import com.example.mcriderkit.data.DataSource

class HazardRepository(private val dao: HazardTestDao) {

    suspend fun insertPresetData() {
        val existingTests = dao.getAllHazardTests()
        if (existingTests.isEmpty()) {
            dao.insertAll(DataSource.presetHazardTests)
        }
    }

    suspend fun getAllHazardTests() = dao.getAllHazardTests()
    suspend fun getHazardTestById(id: Int) = dao.getHazardTestById(id)
    suspend fun updateScore(hazardTest: HazardTest) = dao.updateScore(hazardTest)
}