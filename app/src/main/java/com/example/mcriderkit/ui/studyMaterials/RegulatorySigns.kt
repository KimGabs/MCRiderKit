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
fun RegulatorySign(
    onNextButtonClicked: () -> Unit
) {
    val context = LocalContext.current

    LazyColumn() {
        item{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF4CAF50))
            ){
                Banner(context.getString(R.string.REG_SEC_1))
            }
            Spacer(modifier = Modifier.height(16.dp))
            SectionTitle(context.getString(R.string.PRIORITY_SEC))
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.STOP_SIGN),
                content = listOf(context.getString(R.string.STOP_SIGN_1))
            )
            CenterRSI(R.drawable.sign_stop)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.GIVE_WAY_SIGN),
                content = listOf(context.getString(R.string.GIVE_WAY_SIGN_1))
            )
            CenterRSI(R.drawable.sign_give_way)
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.LEFT_TURNER_SIGN),
                content = listOf(
                    context.getString(R.string.LEFT_TURNER_SIGN_1),
                    context.getString(R.string.LEFT_TURNER_SIGN_2)
                )
            )
            CenterRSI(R.drawable.sign_left_turner)
            Spacer(modifier = Modifier.height(18.dp))
        }


        item{
            SectionContent(
                title = context.getString(R.string.DIRECTIONAL_SEC),
                content = listOf(
                    context.getString(R.string.DIRECTIONAL_1),
                )
            )
        }

        item{
            LabelRSI(context.getString(R.string.DIRECTIONAL_2), R.drawable.sign_pass_either_side)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item{
            RSIRow(
                R.drawable.sign_no_turns_2,
                R.drawable.sign_one_way_2,
                R.drawable.sign_one_way_3,
            )
            Spacer(modifier = Modifier.height(8.dp))
            RSIRow(
                R.drawable.sign_keep_left_2,
                R.drawable.sign_keep_right_2,
                R.drawable.sign_merging_traffic
            )
            Spacer(modifier = Modifier.height(8.dp))
            RSIRow(
                R.drawable.sign_all_traffic_right_2,
                R.drawable.sign_all_traffic_left_2,
                R.drawable.sign_two_way_2
            )
            Spacer(modifier = Modifier.height(8.dp))
            RSIRow(
                R.drawable.sign_two_way_traffic,
                R.drawable.sign_must_turn_right,
                R.drawable.sign_must_turn_left
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(
                title = context.getString(R.string.RESTRICTIVE_SEC),
                content = listOf(
                    context.getString(R.string.RESTRICTIVE_1),
                )
            )
            RSIRow(
                R.drawable.sign_no_entry,
                R.drawable.sign_no_entry_2
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionText(content = listOf(context.getString(R.string.RESTRICTIVE_2)))
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.sign_no_entry_3),
                contentDescription = "Driving Indications",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            SectionContent(context.getString(R.string.NO_PED_CROSS_SIGN),
                content = listOf(context.getString(R.string.NO_PED_CROSS_SIGN_1))
            )
            CenterRSI(R.drawable.sign_no_ped_cross)
            Spacer(modifier = Modifier.height(18.dp))
        }

        item{
            SectionContent(context.getString(R.string.USE_OVERPASS_SIGN),
                content = listOf(context.getString(R.string.USE_OVERPASS_SIGN_1))
            )
            RSIRow(
                R.drawable.sign_use_overpass,
                R.drawable.sign_use_ped_xing,
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        item{
            SectionContent(context.getString(R.string.NO_RL_TURN_SIGN),
                content = listOf(context.getString(R.string.NO_RL_TURN_SIGN_1))
            )
            RSIRow(
                R.drawable.sign_no_left_turn,
                R.drawable.sign_no_right_turn
            )
            Spacer(modifier = Modifier.height(18.dp))
        }

        item{
            LabelRSI(context.getString(R.string.NO_OVERTAKING_SIGN), R.drawable.sign_no_overtaking)
            Spacer(modifier = Modifier.height(18.dp))
        }

        item {
            SectionTitle(title = context.getString(R.string.SPEED_SIGNS))
            LabelRSI(context.getString(R.string.SPEED_SIGNS_1), R.drawable.sign_speed_limit)
            Spacer(modifier = Modifier.height(8.dp))
            LabelRSI(context.getString(R.string.SPEED_SIGNS_2), R.drawable.sign_end_speed_limit)
            Spacer(modifier = Modifier.height(8.dp))
            LabelRSI(context.getString(R.string.SPEED_SIGNS_3), R.drawable.sign_min_speed_limit)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item{
            SectionTitle(title = context.getString(R.string.PARKING_SIGNS))
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            LabelRSI(context.getString(R.string.PARKING_SIGNS_1), R.drawable.sign_allowed_park_time)
            LabelRSI(context.getString(R.string.PARKING_SIGNS_2), R.drawable.sign_no_stopping)
            LabelRSI(context.getString(R.string.PARKING_SIGNS_3), R.drawable.sign_no_loading)
            LabelRSI(context.getString(R.string.PARKING_SIGNS_4), R.drawable.sign_fire_hydrant)
            LabelRSI(context.getString(R.string.PARKING_SIGNS_5), R.drawable.sign_no_waiting)
        }

        item{
            CenterRSI(R.drawable.sign_bus_puj_stop)
            SectionContent(
                title = context.getString(R.string.PARKING_SIGNS_6),
                content = listOf(context.getString(R.string.PARKING_SIGNS_6_1)))
            Spacer(modifier = Modifier.height(16.dp))
            CenterRSI(R.drawable.sign_bus_stop)
            SectionContent(
                title = context.getString(R.string.PARKING_SIGNS_7),
                content = listOf(context.getString(R.string.PARKING_SIGNS_6_1)))
            Spacer(modifier = Modifier.height(16.dp))
            CenterRSI(R.drawable.sign_puj_stop)
            SectionContent(
                title = context.getString(R.string.PARKING_SIGNS_8),
                content = listOf(context.getString(R.string.PARKING_SIGNS_6_1)))
            Spacer(modifier = Modifier.height(16.dp))
            CenterRSI(R.drawable.sign_tow_away)
            SectionContent(
                title = context.getString(R.string.PARKING_SIGNS_9),
                content = listOf(context.getString(R.string.PARKING_SIGNS_9_1)))

            LabelRSI(context.getString(R.string.PARKING_SIGNS_10), R.drawable.sign_block_intersection)
            LabelRSI(context.getString(R.string.PARKING_SIGNS_11), R.drawable.sign_no_park_week)

            Spacer(modifier = Modifier.height(16.dp))

            CenterRSI(R.drawable.sign_broken_lines)
            SectionContent(
                title = context.getString(R.string.PARKING_SIGNS_12),
                content = listOf(context.getString(R.string.PARKING_SIGNS_12_1)))
            Spacer(modifier = Modifier.height(16.dp))
        }

        item{
            NextButton(onNextButtonClicked)
        }
    }
}