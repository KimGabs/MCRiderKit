package com.example.mcriderkit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.data.QuizScore
import com.example.mcriderkit.ui.components.HazardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HazardTestViewModel(private val repository: HazardRepository) : ViewModel() {
    private val _hazardTests = MutableStateFlow<List<HazardTest>>(emptyList())
    val hazardTests: StateFlow<List<HazardTest>> = _hazardTests

    val trophyTests: StateFlow<List<HazardTest>> = hazardTests
        .map { list -> list.filter { it.trophy } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    private val _selectedHazardTest = MutableStateFlow<HazardTest?>(null)
    val selectedHazardTest: StateFlow<HazardTest?> = _selectedHazardTest

    init {
        viewModelScope.launch {
            repository.insertPresetData() // Insert preset data if needed
        }
    }

    fun loadHazardTests() {
        viewModelScope.launch {
            _hazardTests.value = repository.getAllHazardTests()
        }
    }

    fun selectHazardTest(id: Int) {
        viewModelScope.launch {
            _selectedHazardTest.value = repository.getHazardTestById(id)
        }
    }

    fun updateScore(hazardTest: HazardTest) {
        viewModelScope.launch {
            repository.updateScore(hazardTest)
        }
    }
}
