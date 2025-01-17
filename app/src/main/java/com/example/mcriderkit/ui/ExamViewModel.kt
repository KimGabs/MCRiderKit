package com.example.mcriderkit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.data.DataSource.Question
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
        viewModelScope.launch {
            // Retrieve the existing score for the given quizType
            val existingScore = repository.getScoreByType(quizType)

            if (existingScore == null) {
                // No existing score, insert a new record
                repository.insertOrUpdateScore(QuizScore(quizType = quizType, highestScore = score))
            } else if (score > existingScore.highestScore) {
                // Update the score only if the new score is higher
                repository.insertOrUpdateScore(existingScore.copy(highestScore = score))
            }
        }
    }

    private val _examState = MutableStateFlow(ExamUiState())
    val examState: StateFlow<ExamUiState> = _examState.asStateFlow()

    private var _questions: List<Question> = emptyList()
    val questions: List<Question>
        get() = _questions

    init {
        initializeQuestions()
    }

    private fun initializeQuestions() {
        _questions = DataSource.examQuestions.shuffled() // Shuffle at initialization
    }

    fun resetQuiz() {
        initializeQuestions()
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

