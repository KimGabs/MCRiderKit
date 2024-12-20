package com.example.mcriderkit.data

import androidx.room.*

@Dao
interface HazardTestDao {

    @Query("SELECT * FROM hazard_tests")
    suspend fun getAllHazardTests(): List<HazardTest>

    @Query("SELECT * FROM hazard_tests WHERE id = :id")
    suspend fun getHazardTestById(id: Int): HazardTest?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHazardTest(hazardTest: HazardTest)

    @Update
    suspend fun updateScore(hazardTest: HazardTest)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(hazardTests: List<HazardTest>)

}

