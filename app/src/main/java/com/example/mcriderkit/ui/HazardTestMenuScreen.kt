package com.example.mcriderkit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.sp

@Composable
fun HazardTestMenuScreen(
    viewModel: HazardTestViewModel,
    onClipSelected: (Int) -> Unit // This callback should accept the 'id' or 'index'
) {
    // Collecting the list of hazard tests
    val hazardTestsState = viewModel.hazardTests.collectAsState()

    // Accessing the list of hazard tests from the State
    val hazardTests = hazardTestsState.value

    // Load hazard tests once when the Composable is first launched
    LaunchedEffect(Unit) {
        viewModel.loadHazardTests()
    }

    // LazyColumn to display hazard test items
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()) ) {
        for (test in hazardTests) {
            // Replace test.name with whatever property you need to display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onClipSelected(test.id) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = test.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = "Location: ${test.location}")
                    Text(text = "Speed Limit: ${test.speedLimit}")
                    Text(text = "Time of Day: ${test.timeOfDay}")
                    Text(text = "Weather: ${test.weather}")
                }
            }

            // Spacer to add spacing between items
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

