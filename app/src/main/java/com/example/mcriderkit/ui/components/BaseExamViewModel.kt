package com.example.mcriderkit.ui.components

import androidx.lifecycle.ViewModel
import com.example.mcriderkit.data.ExamUiState
import kotlinx.coroutines.flow.StateFlow


abstract class BaseExamViewModel : ViewModel() {
    abstract val highestScore: StateFlow<Int?>
    abstract val examState: StateFlow<ExamUiState>

    abstract fun fetchHighestScore(quizType: String)
    abstract fun resetQuiz()
}
