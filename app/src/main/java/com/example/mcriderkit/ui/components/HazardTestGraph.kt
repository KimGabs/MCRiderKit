package com.example.mcriderkit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.mcriderkit.data.HazardTest
import com.example.mcriderkit.ui.HazardTestViewModel

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
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (hazardTests.isNotEmpty()) {
                LineChartView(hazardTests)
            } else {
                Text("No data available")
            }
        }
    }
}

@Composable
fun LineChartView(hazardTests: List<HazardTest>) {
    if (hazardTests.isEmpty()) {
        Text("No data available", style = MaterialTheme.typography.bodyMedium)
        return
    }

    // Prepare data for the chart
    val points = hazardTests.mapIndexed { index, test ->
        Point(index.toFloat(), test.lastScore.toFloat()) // Y-values (lastScore)
    }

    // Define X-axis (Test IDs or Numbers)
    val xAxisData = AxisData.Builder()
        .axisStepSize(80.dp) // Adjust step size for spacing
        .steps(points.size - 1) // Number of test points
        .labelData { index -> "Test ${hazardTests[index].id}" } // Display integer values
        .build()

    // Define Y-axis (Score from 0 to 100)
    val yAxisData = AxisData.Builder()
        .axisStepSize(100.dp / 5) // Adjust step size for spacing
        .steps(5) // 5 steps (e.g., 0, 20, 40, 60, 80, 100)
        .build()

    // Define the line chart data
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = points,
                    LineStyle(color = Color.Gray, width = 0.5f),
                    IntersectionPoint(color = Color.Green),
                    SelectionHighlightPoint(color = Color.Green),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(listOf(Color.Gray, Color.Transparent))
                    ),
                    SelectionHighlightPopUp(
                        popUpLabel = { x, y ->
                            val xLabel = "Test ${x.toInt() + 1},"
                            val yLabel = "Score: ${String.format("%.2f", y)}"
                            "$xLabel  $yLabel"
                        }
                    )
                )
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = Color.LightGray),
        backgroundColor = Color.White
    )

    // Wrap the LineChart in a Box with padding to prevent label truncation
    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )

}