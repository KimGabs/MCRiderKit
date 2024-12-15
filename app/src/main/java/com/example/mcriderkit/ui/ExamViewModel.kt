package com.example.mcriderkit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.data.ExamUiState
import com.example.mcriderkit.data.QuizScore
import com.example.mcriderkit.data.QuizScoreDao
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExamViewModel(
) : ViewModel() {
    private val _examState = MutableStateFlow(ExamUiState())

    private val _highestScore = MutableStateFlow(0)
    val highestScore: StateFlow<Int> = _highestScore

    val examState: StateFlow<ExamUiState> = _examState.asStateFlow()
    val questions = DataSource.examQuestions

    fun resetQuiz() {
        _examState.value = ExamUiState() // Reset state
    }

    fun selectAnswer(answerIndex: Int) {
        _examState.update { currentState ->
            currentState.copy(selectedAnswer = answerIndex)
        }
    }

    fun submitAnswer() {
        _examState.update { currentState ->
            currentState.copy(hasSubmitted = true)
        }
    }

    fun finalQuestion(){
        _examState.update { currentState ->
            val currentQuestion = questions[currentState.currentQuestionIndex]
            val selectedAnswer = currentState.selectedAnswer
            val updatedScore = if (selectedAnswer == currentQuestion.correctAnswerIndex) {
                currentState.score + 1
            } else {
                currentState.score
            }

            currentState.copy(
                isExamComplete = true,
                totalQuestions = questions.size,
                score = updatedScore,
                hasSubmitted = false // Reset for next quiz
            )
        }
    }

    fun nextQuestion() {
        _examState.update { currentState ->
            val currentQuestion = questions[currentState.currentQuestionIndex]
            val selectedAnswer = currentState.selectedAnswer
            val updatedScore = if (selectedAnswer == currentQuestion.correctAnswerIndex) {
                currentState.score + 1
            } else {
                currentState.score
            }

            val nextIndex = currentState.currentQuestionIndex + 1
            if (nextIndex >= questions.size) {
                currentState.copy(
                    isExamComplete = true,
                    totalQuestions = questions.size,
                    score = updatedScore,
                    hasSubmitted = false // Reset for next quiz
                )

            } else {
                currentState.copy(
                    currentQuestionIndex = nextIndex,
                    selectedAnswer = null,
                    score = updatedScore,
                    hasSubmitted = false
                )
            }
        }
    }
}

