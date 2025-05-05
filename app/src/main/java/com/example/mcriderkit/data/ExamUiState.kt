package com.example.mcriderkit.data

data class ExamUiState(
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: Int? = null,
    val score: Int = 0,
    val isExamComplete: Boolean = false,
    val totalQuestions: Int = 1,
    val hasSubmitted: Boolean = false
)
