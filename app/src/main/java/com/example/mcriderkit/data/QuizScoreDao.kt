package com.example.mcriderkit.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizScoreDao {
    @Upsert
    suspend fun upsert(quizScore: QuizScore)

    // Get all high scores for all quiz types
    @Query("SELECT * FROM quiz_scores ORDER BY category ASC")
    suspend fun getAllScores(): Flow<List<QuizScore>>

    // Get the highest score for a specific quiz type
    @Query("SELECT * FROM quiz_scores WHERE category = :category LIMIT 1")
    suspend fun getScoreByType(category: String): QuizScore?

    @Query("DELETE FROM quiz_scores WHERE category = :category")
    suspend fun deleteScoreByQuizType(category: String)

    @Query("DELETE FROM quiz_scores")
    suspend fun clearAllScores()
}

