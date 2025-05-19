package com.example.mcriderkit.ui.components

import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.data.QuizScore
import com.example.mcriderkit.data.QuizScoreDao

class QuizRepository(private val quizScoreDao: QuizScoreDao) {
    suspend fun getScoreByType(quizType: String): QuizScore? {
        return quizScoreDao.getScoreByType(quizType)
    }

    suspend fun insertOrUpdateScore(quizScore: QuizScore) {
        quizScoreDao.insertOrUpdateScore(quizScore)
    }

    /*suspend fun insertPresetQuizScores() {
        val existingTests = quizScoreDao.getAllScores()
        if (existingTests.isEmpty()) {
            quizScoreDao.insertAll(DataSource.presetQuizScore)
        }
    }*/

    suspend fun updateTrophyIfPerfect(quizType: String, perfectScore: Int) {
        quizScoreDao.updateTrophyIfPerfect(
            quizType = quizType,
            perfectScore = perfectScore
        )
    }

    suspend fun getAllScores(): List<QuizScore> {
        return quizScoreDao.getAllScores()
    }
}