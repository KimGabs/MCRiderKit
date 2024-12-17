package com.example.mcriderkit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.data.ExamUiState
import com.example.mcriderkit.data.QuizScore
import com.example.mcriderkit.data.QuizScoreDao
import com.example.mcriderkit.ui.components.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExamViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    private val _highestScore = MutableStateFlow<Int?>(null)
    val highestScore: StateFlow<Int?> = _highestScore

    fun fetchHighestScore(quizType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val score = repository.getScoreByType(quizType)
            _highestScore.value = score?.highestScore
        }
    }

    fun saveScore(quizType: String, score: Int) {
        // Check if the score already exists for the given quizType
        viewModelScope.launch {
            val existingScore = repository.getScoreByType(quizType)
            if (existingScore != null) {
                // Update the score if it already exists
                repository.insertOrUpdateScore(existingScore.copy(highestScore = score))
            } else {
                // Insert the new score if it doesn't exist
                repository.insertOrUpdateScore(QuizScore(quizType = quizType, highestScore = score))
            }
        }
    }

    private val _examState = MutableStateFlow(ExamUiState())
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

            saveScore("Non-Pro", updatedScore)

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

