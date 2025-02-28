package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R
import com.example.mcriderkit.ui.MenuButton


@Composable
fun RoadSignsScreen(
    roadSignsList: List<String>,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        itemsIndexed(roadSignsList) { index, item ->
            MenuButton(
                item = item,
                onClick = { onNextButtonClicked(index) },
                modifier = Modifier
                    .aspectRatio(1f) // Makes buttons square
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

@Composable
fun RSIRow(vararg imageResIds: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center // Centers items horizontally within the Row
    ) {
        imageResIds.forEach { imageRes ->
            RoadSignItem(imageRes = imageRes)
        }
    }
}

@Composable
fun RoadSignItem(imageRes: Int) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Sign image",
        modifier = Modifier
            .size(140.dp)
            .padding(2.dp, 8.dp) // Optional padding around each item
    )
}

@Composable
fun CenterRSI(imageRes: Int) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center // Centers content within the Box
    ) {
        RoadSignItem(imageRes = imageRes)
    }
}

@Composable
fun LabelRSI(label: String, imageRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        RoadSignItem(imageRes = imageRes)
    }
}