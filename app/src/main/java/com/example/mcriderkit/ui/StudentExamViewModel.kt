package com.example.mcriderkit.ui

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.mcriderkit.data.DataSource
import com.example.mcriderkit.data.DataSource.Question
import com.example.mcriderkit.data.ExamUiState
import com.example.mcriderkit.data.QuizScore
import com.example.mcriderkit.ui.components.AchievementManager
import com.example.mcriderkit.ui.components.BaseExamViewModel
import com.example.mcriderkit.ui.components.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentExamViewModel(
    private val repository: QuizRepository,
) : BaseExamViewModel() {

    private val _highestScore = MutableStateFlow<Int?>(null)
    override val highestScore: StateFlow<Int?> = _highestScore
    private var totalQuestions = 0

    private val _quizScores = MutableStateFlow<List<QuizScore>>(emptyList())
    val quizScores: StateFlow<List<QuizScore>> = _quizScores

    fun loadQuizScores() {
        viewModelScope.launch {
            _quizScores.value = repository.getAllScores()
        }
    }

    override fun fetchHighestScore(quizType: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val score = repository.getScoreByType(quizType)
            _highestScore.value = score?.highestScore
        }
    }

    init {
        viewModelScope.launch {
            repository.insertPresetQuizScores() // Insert preset data if needed
        }
    }

    fun saveScore(quizType: String, score: Int) {
        viewModelScope.launch {
            // Retrieve the existing score for the given quizType
            val existingScore = repository.getScoreByType(quizType)

            if (existingScore == null) {
                // No existing score, insert new one
                repository.insertOrUpdateScore(
                    QuizScore(quizType = quizType, highestScore = score)
                )
                if (score == totalQuestions) {
                    repository.updateTrophyIfPerfect(quizType = quizType, totalQuestions)
                }
            } else {
                // Update only if the new score is higher
                if (score > existingScore.highestScore) {
                    repository.insertOrUpdateScore(existingScore.copy(highestScore = score))

                    if (!existingScore.trophy &&
                        quizType == existingScore.quizType &&
                        score == totalQuestions
                    ) {
                        repository.updateTrophyIfPerfect(quizType = quizType, totalQuestions)
                    }
                }
            }
        }
    }

    private val _examState = MutableStateFlow(ExamUiState())
    override val examState: StateFlow<ExamUiState> = _examState.asStateFlow()

    private var _questions: List<Question> = emptyList()
    val questions: List<Question>
        get() = _questions

    init {
        initializeQuestions()
    }

    private fun initializeQuestions() {
        _questions = DataSource.examQuestions.shuffled().take(40) // Shuffle at initialization
        totalQuestions = _questions.size
    }

    override fun resetQuiz() {
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

    fun finalQuestion(examType: String) {
        _examState.update { currentState ->
            val currentQuestion = questions[currentState.currentQuestionIndex]
            val selectedAnswer = currentState.selectedAnswer
            val updatedScore = if (selectedAnswer == currentQuestion.correctAnswerIndex) {
                currentState.score + 1
            } else {
                currentState.score
            }

            saveScore(examType, updatedScore)

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
