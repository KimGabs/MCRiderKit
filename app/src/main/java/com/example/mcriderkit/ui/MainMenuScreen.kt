package com.example.mcriderkit.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun MainMenuScreen(
    menuList: List<String>,
    onNextButtonClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.play_store_512),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF6A3DE8))
                .padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ){
            menuList.forEachIndexed { index, item ->
                SelectMenuButton(
                    item = item,
                    onClick = { onNextButtonClicked(index) }
                )
            }
        }
    }
}

@Composable
fun SelectMenuButton(
    item: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.85f)
            .height(56.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp)
    ) {
        Text(
            text = item,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}