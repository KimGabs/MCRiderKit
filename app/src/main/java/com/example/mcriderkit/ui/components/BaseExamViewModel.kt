package com.example.mcriderkit.ui.components

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mcriderkit.data.ExamUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseExamViewModel : ViewModel() {
    abstract val highestScore: StateFlow<Int?>
    abstract val examState: StateFlow<ExamUiState>

    private val _showTrophyDialog = MutableStateFlow(false)
    val showTrophyDialog: StateFlow<Boolean> get() = _showTrophyDialog

    abstract fun fetchHighestScore(quizType: String)
    abstract fun resetQuiz()

    private var hasAchievedTrophy = false  // Track if the trophy has been awarded

    fun checkQuizResult(score: Int, totalQuestions: Int): Boolean {
        return score == totalQuestions
    }

    fun checkAndShowTrophyDialog(score: Int, totalQuestions: Int) {
        // Ensure that the dialog is shown only for a perfect score
        if (checkQuizResult(score, totalQuestions) && !hasAchievedTrophy) {
            _showTrophyDialog.value = true
            hasAchievedTrophy = true  // Set the flag to true to prevent showing the trophy again
        }
        else {
            Log.d("QuizViewModel", "Score: $score, Total Questions: $totalQuestions")
            Log.d("QuizViewModel", "Has Achieved Trophy: $hasAchievedTrophy")
        }
    }

    fun hideTrophyDialog() {
        _showTrophyDialog.value = false
    }

}
