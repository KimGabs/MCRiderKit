package com.example.mcriderkit.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.ui.HazardTestViewModel
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties

@Composable
fun HazardTestGraph(viewModel: HazardTestViewModel) {

    // Observe the hazardTests StateFlow
    val hazardTests by viewModel.hazardTests.collectAsState()

    // Load data when the screen is first launched
    LaunchedEffect(Unit) {
        viewModel.loadHazardTests()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Hazard Test Scores",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (hazardTests.isNotEmpty()) {
                HazardTestBarChart(hazardTests)
            } else {
                Text("No data available")
            }
        }
    }
}

@Composable
fun HazardTestBarChart(hazardTests: List<HazardTest>) {
    // Prepare data for the bar chart
    val barData = remember(hazardTests) {
        hazardTests.map { test ->
            Bars(
                label = "Test ${test.id}", // X-axis label for each bar
                values = listOf(
                    Bars.Data(

                        value = test.lastScore.toDouble(), // Y-axis value: lastScore
                        color = Brush.verticalGradient(
                            colors = listOf(Color(0xFFCDDC39), Color(0xFF4CAF50)), // Bar color
                            startY = 0.0f,
                            endY = 500.0f
                        )
                    )
                )
            )
        }
    }

    // Display the bar chart
    ColumnChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 20.dp),
        data = barData,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp), // Rounded corners
            spacing = 6.dp, // Spacing between bars
            thickness = 30.dp // Bar width
        ),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        labelHelperProperties = LabelHelperProperties(
            enabled = false
        )
    )
}