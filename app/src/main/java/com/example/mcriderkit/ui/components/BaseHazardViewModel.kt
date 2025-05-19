package com.example.mcriderkit.ui.components

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BaseHazardViewModel : ViewModel() {
    private val _showHazardTrophyDialog = MutableStateFlow(false)
    val showHazardTrophyDialog: StateFlow<Boolean> get() = _showHazardTrophyDialog

    private var hasAchievedTrophy = false  // Track if the trophy has been awarded

    fun checkHazardResult(score: Int): Boolean {
        return score == 100
    }

    fun hazardShowTrophyDialog(score: Int) {
        // Ensure that the dialog is shown only for a perfect score
        if (checkHazardResult(score) && !hasAchievedTrophy) {
            _showHazardTrophyDialog.value = true
            hasAchievedTrophy = true  // Set the flag to true to prevent showing the trophy again
        }
    }

    fun hideTrophyDialog() {
        _showHazardTrophyDialog.value = false
    }

}

