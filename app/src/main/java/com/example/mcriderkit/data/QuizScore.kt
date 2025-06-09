package com.example.mcriderkit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_scores")
data class QuizScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quizType: String,
    val highestScore: Int = 0,
    val trophy: Boolean = false
)