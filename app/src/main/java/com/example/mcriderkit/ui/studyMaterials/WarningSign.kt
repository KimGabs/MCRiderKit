package com.example.mcriderkit.ui.studyMaterials

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.mcriderkit.R

@Composable
fun WarningSign(
    onNextButtonClicked: () -> Unit,
    onPrevButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ){
                Banner(context.getString(R.string.WARN_SEC))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Horizontal Warning Signs
        item{
            SectionContent(
                title = context.getString(R.string.HOR_SEC),
                content = listOf(context.getString(R.string.HOR_SEC_1))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.sign_horizontal),
                contentDescription = "Driving Indications",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Intersection Signs
        item{
            SectionContent(
                title = context.getString(R.string.INT_SEC),
                content = listOf(
                    context.getString(R.string.INT_SEC_1),
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.sign_intersections),
                contentDescription = "Driving Indications",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Intersection Signs
        item{
            SectionContent(
                title = context.getString(R.string.ADV_SEC),
                content = listOf(
                    context.getString(R.string.ADV_SEC_1)
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Advance Warning Signs
        item{
            SectionContent(
                title = context.getString(R.string.ADV_SEC_2),
                content = listOf(
                    context.getString(R.string.ADV_SEC_2_1)
                )
            )
            CenterRSI(R.drawable.sign_traffic_light_ahead)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.sign_warning_ahead),
                contentDescription = "Driving Indications",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Road Width Signs
        item{
            SectionTitle(title = context.getString(R.string.RW_SEC))
            Image(
                painter = painterResource(id = R.drawable.sign_road_width),
                contentDescription = "Road Width Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Road Obstacle Signs
        item{
            SectionTitle(title = context.getString(R.string.OBS_SEC))
            Image(
                painter = painterResource(id = R.drawable.sign_obstacles),
                contentDescription = "Road Obstacle Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Pedestrian Signs
        item {
            SectionTitle(title = context.getString(R.string.PEDS_SEC))
            LabelRSI(context.getString(R.string.PEDS_SEC_1), R.drawable.sign_pedestrian_crossing_2)
            LabelRSI(context.getString(R.string.PEDS_SEC_2), R.drawable.sign_pedestrian_crossing)
            LabelRSI(context.getString(R.string.PEDS_SEC_3), R.drawable.sign_pedestrian_crossing_3)
            LabelRSI(context.getString(R.string.PEDS_SEC_4), R.drawable.sign_pedxing)
            LabelRSI(context.getString(R.string.PEDS_SEC_5), R.drawable.sign_bike_lane)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Railway Level Crossing Signs
        item{
            SectionTitle(title = context.getString(R.string.RAIL_SEC))
            LabelRSI(context.getString(R.string.RAIL_SEC_1), R.drawable.sign_railroad_crossing)
            LabelRSI(context.getString(R.string.RAIL_SEC_2), R.drawable.sign_railroad_crossing_2)
            LabelRSI(context.getString(R.string.RAIL_SEC_3), R.drawable.sign_railroad_crossing_3)
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Supplementary Signs
        item{
            SectionTitle(title = context.getString(R.string.SUP_SEC))
            Image(
                painter = painterResource(id = R.drawable.sign_supplementary),
                contentDescription = "Supplementary Signs",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Previous and Next Button
        item{
            PrevNextButton(
                onPrevButtonClicked = onPrevButtonClicked,
                onNextButtonClicked = onNextButtonClicked
            )
        }
    }
}