package com.example.mcriderkit.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizScoreDao {
    @Query("SELECT * FROM quiz_scores WHERE quizType = :quizType LIMIT 1")
    suspend fun getScoreByType(quizType: String): QuizScore?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateScore(quizScore: QuizScore)

    @Query("SELECT * FROM quiz_scores ORDER BY highestScore DESC")
    suspend fun getAllScores(): List<QuizScore>
}