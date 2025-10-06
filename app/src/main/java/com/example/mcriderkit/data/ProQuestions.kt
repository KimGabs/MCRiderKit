/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mcriderkit.data

import androidx.compose.ui.platform.LocalContext
import com.example.mcriderkit.R
import com.example.mcriderkit.data.DataSource.Question

object ProQuestions {

    data class Question(
        val question: String, // The text-based question
        val imageResId: Int? = null, // Optional image resource (null if no image)
        val options: List<String>, // List of answer options
        val correctAnswerIndex: Int, // Index of the correct answer
    )

    val proMotExamQuestions = listOf(
        Question(
            "Is it proper to negotiate with an enforcer if apprehended with a violation?",
            options = listOf(
                "Yes, to avoid getting a ticket",
                "Yes, if the violation is minor",
                "No, it is improper for any driver or any traffic enforcer to negotiate a violation",
                "Yes, if the enforcer allows it"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "This is one of the qualifications of a professional driver:",
            options = listOf(
                "Must be able to read and write",
                "Must own a private vehicle",
                "Must be at least a college graduate",
                "Must have a business permit"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "A proof that a motorcycle helmet is within quality standard:",
            options = listOf(
                "A custom logo printed on the helmet",
                "ICC or PS stickers attached at the back of the helmet",
                "A receipt from the motorcycle shop",
                "A reflective tape placed on the helmet"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "While on duty, a professional tricycle driver must:",
            options = listOf(
                "Wear proper uniform",
                "Wear casual clothes of choice",
                "Remove the shirt while driving",
                "Wear slippers and shorts for comfort"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "When diesel fuel or oil is spilled on the road, it is dangerous to all motorists, particularly:",
            options = listOf(
                "Car drivers",
                "Bus drivers",
                "Motorcyclists",
                "Truck drivers"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "Operators of public utility vehicles can secure franchise or CPC from LTFRB except:",
            options = listOf(
                "Buses",
                "Taxis",
                "Tricycles",
                "Jeepneys"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "It is advisable to use __________ when riding a motorcycle at night.",
            options = listOf(
                "Dark clothing",
                "A jacket without reflectors",
                "Black helmet and gloves",
                "Bright clothing"
            ),
            correctAnswerIndex = 3,
        ),
        Question(
            "What is the penalty for allowing passengers to ride on top or cover of a side car?",
            options = listOf(
                "Warning only",
                "License suspension",
                "Community service",
                "Monetary fines"
            ),
            correctAnswerIndex = 3,
        ),
        Question(
            "While driving on the road and you are about to slow down, you must check for:",
            options = listOf(
                "Pedestrians on the sidewalk",
                "Traffic lights ahead",
                "Vehicles behind to avoid collision",
                "Road signs on the side"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "Safety in driving a motorcycle is a must, thus you should:",
            options = listOf(
                "Wear standard helmet and proper protective gear",
                "Wear slippers and shorts for comfort",
                "Ride without a helmet for better visibility",
                "Avoid using gloves and jackets while driving"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "At times when the vehicle in front of you does NOT give way, you must:",
            options = listOf(
                "Use your horn repeatedly to force them to move",
                "Be patient and do NOT overtake",
                "Overtake immediately to save time",
                "Drive closely to pressure the vehicle ahead"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "In stopping, it is always safe to:",
            options = listOf(
                "Use the rear brake only",
                "Use the front brake only",
                "Use the front and rear brakes at the same time",
                "Turn off the engine to slow down"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "In making a U-turn, it is NOT advisable to use ___________",
            options = listOf(
                "The accelerator",
                "The clutch",
                "The front wheel brake",
                "The signal light"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "In a merging road or traffic, you must check _______________",
            options = listOf(
                "Your phone for directions",
                "Your speed, brakes, side mirrors and signals",
                "The vehicles behind only",
                "The condition of the road markings"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "The feet of a rider while driving a motorcycle must:",
            options = listOf(
                "Hang freely on the sides",
                "Firmly step on the footrests",
                "Be used to balance while moving",
                "Rest on the ground while riding"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "The fare matrix of tricycles is approved by the ___________",
            options = listOf(
                "Department of Transportation",
                "Land Transportation Office",
                "Land Transportation Franchising and Regulatory Board",
                "Local Government Unit"
            ),
            correctAnswerIndex = 3,
        ),
        Question(
            "A tricycle driver must follow the prescribed:",
            options = listOf(
                "Passenger capacity",
                "Traffic signs",
                "Fare matrix",
                "Uniform color"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "The law prohibits riders to use:",
            options = listOf(
                "The sidewalks and center island",
                "The rightmost lane of the road",
                "The designated motorcycle lane",
                "The pedestrian crossing during green light"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "Aside from the Motorized Tricycle Operator’s Permit (MTOP), the following documents must be visible:",
            options = listOf(
                "Driver’s license and vehicle insurance",
                "Identification Card and Fare Matrix",
                "Vehicle registration and plate number",
                "Franchise certificate and inspection report"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "Is there a need to apply for a CPC from LTFRB prior to operating a tricycle for hire?",
            options = listOf(
                "Yes, all public vehicles must secure a CPC from LTFRB",
                "No. Tricycle operators shall secure permits from concerned local government unit",
                "Yes, but only for tricycles with sidecars",
                "Only if the tricycle operates outside city limits"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "The allowable motorcycle engine displacement for expressways is:",
            options = listOf(
                "150cc and above",
                "250cc and above",
                "400cc and above",
                "Below 400cc with permit"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "Can a motorcyclist stop in a yellow box?",
            options = listOf(
                "Yes, only when waiting for the traffic light to change",
                "Yes, if there are no vehicles approaching",
                "At any instance a rider is not allowed to stop inside a yellow box",
                "Yes, when dropping off a passenger"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "Is it safe to use your handheld phone while driving a motorcycle?",
            options = listOf(
                "Yes, if traffic is light",
                "Yes, when using loudspeaker mode",
                "No, it is against the law (R.A. No. 10913 Anti-Distracted Driving Act)",
                "Yes, if the phone is used for navigation"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "Broken yellow lines allow motorists:",
            options = listOf(
                "To overtake and cross when it is safe",
                "To never cross under any circumstance",
                "To park temporarily on the side of the road",
                "To speed up without caution"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "When you see an intersection with a blank inverted triangle traffic sign, you should:",
            options = listOf(
                "Slow down and give way to any vehicle in the intersection",
                "Speed up to pass before other vehicles",
                "Stop completely even if the road is clear",
                "Ignore it since it has no text or symbol"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "One of the requirements of installing a custom-made top box must be:",
            options = listOf(
                "1 foot x 1 foot x 1 foot",
                "3 feet x 2 feet x 1 foot",
                "2 feet x 2 feet x 2 feet",
                "4 feet x 2 feet x 3 feet"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "Can a rider place a blinker taillight in his/her motorcycle?",
            options = listOf(
                "Yes, if it improves visibility",
                "Yes, during nighttime only",
                "No, it is illegal modification (Joint Administrative Order No. 2014-01)",
                "Yes, if the motorcycle is used for delivery"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "The allowable color of motorcycle headlight is:",
            options = listOf(
                "Blue or green",
                "Red or orange",
                "Yellowish white or yellow",
                "White with blinking pattern"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "The color of brake light for motorcycle must be __________",
            options = listOf(
                "Amber",
                "Bright red",
                "White",
                "Yellow"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "What is the allowable number of bulbs for every auxiliary lamp (LED)?",
            options = listOf(
                "Four (4)",
                "Eight (8)",
                "Six (6)",
                "Ten (10)"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "If you are feeling tired and sleepy, you must:",
            options = listOf(
                "Stop and take a rest",
                "Drink coffee and continue driving",
                "Drive faster to reach your destination sooner",
                "Open the window for fresh air and keep driving"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "A motorcyclist must not exceed the ________ Blood Alcohol Content when driving:",
            options = listOf(
                "0.03%",
                "0.05%",
                "0.00%",
                "0.08%"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "What is the exemption for a child to ride a motorcycle pursuant to R.A. No. 10666 Children’s Safety on Motorcycle Act?",
            options = listOf(
                "When the child is wearing a helmet",
                "When the road has light traffic",
                "When the child to be transported requires immediate medical attention",
                "When accompanied by both parents"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "What is the risk of driving alongside a car?",
            options = listOf(
                "It is prone to road crash due to blind spots",
                "It allows better visibility of the road ahead",
                "It helps maintain a safer following distance",
                "It improves communication with the car driver"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "Sleeping early and being physically fit is a:",
            options = listOf(
                "Good professional driver's trait",
                "Sign of being a beginner driver",
                "Requirement only for long trips",
                "Minor factor in road safety"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "When driving a motorcycle with a back rider, one must anticipate:",
            options = listOf(
                "Shorter braking distance",
                "Faster acceleration",
                "Longer braking distance",
                "Reduced tire traction"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "An orange traffic sign means:",
            options = listOf(
                "There is roadwork ahead and you must follow the prescribed speed limit",
                "Pedestrian crossing ahead, slow down",
                "Regulatory sign, follow instructions strictly",
                "Warning sign for animal crossing ahead"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "If you are hesitant to overtake the vehicle in front, you must:",
            options = listOf(
                "Overtake quickly to avoid delay",
                "Use your horn and proceed to overtake",
                "Speed up to pass before the next vehicle approaches",
                "Not overtake"
            ),
            correctAnswerIndex = 3,
        ),
        Question(
            "A solid yellow line prohibits a motorist to ________:",
            options = listOf(
                "Overtake",
                "Slow down",
                "Turn left",
                "Change lane when safe"
            ),
            correctAnswerIndex = 0,
        ),

        Question(
            "If two passengers insist to back ride with you, what should you do?",
            options = listOf(
                "Allow both if the road is clear",
                "Allow both if the motorcycle is large",
                "Allow both for a short distance only",
                "Allow only one back rider"
                ),
            correctAnswerIndex = 3,
        ),
        Question(
            "The right time to check your side mirror is:",
            options = listOf(
                "Only when overtaking another vehicle",
                "Only when making a turn",
                "After reaching full speed",
                "Upon boarding and before moving"
            ),
            correctAnswerIndex = 3,
        ),
        Question(
            "When you are behind a longer vehicle, you must ___________ to improve your visibility.",
            options = listOf(
                "Move closer to the vehicle",
                "Flash your lights to signal the driver",
                "Sound your horn to get attention",
                "Stay further back"
                ),
            correctAnswerIndex = 3,
        ),
        Question(
            "When riding with a group, what is the correct hand signal for 'Follow Me'?",
            options = listOf(
                "Left hand pointing forward",
                "Right hand pointing upward",
                "Left hand raised with a circular motion",
                "Left hand straight up, palm forward"
                ),
            correctAnswerIndex = 3,
        ),
        Question(
            "The inverted triangle with red border means:",
            options = listOf(
                "Stop immediately",
                "No entry ahead",
                "Yield the right-of-way",
                "School zone ahead"
            ),
            correctAnswerIndex = 2,
        ),
        Question(
            "The prescribed length of a saddle box is:",
            options = listOf(
                "It must be at least half the length of the motorcycle",
                "It must not exceed the tail end of the motorcycle",
                "It must be equal to the width of the seat",
                "It must not exceed 1 meter in total length"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "The prescribed width of a saddle box is:",
            options = listOf(
                "It must not exceed fourteen (14) inches from the sides of the motorcycle",
                "It must be at least twenty (20) inches wide",
                "It must match the width of the handlebars",
                "It must not exceed ten (10) inches from the sides of the motorcycle"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "The prescribed width of a saddle box is:",
            options = listOf(
                "It must not exceed fourteen (14) inches from the sides of the motorcycle",
                "It must be at least twenty (20) inches wide",
                "It must match the width of the handlebars",
                "It must not exceed ten (10) inches from the sides of the motorcycle"
            ),
            correctAnswerIndex = 0,
        ),
        Question(
            "It shows the engine revolution per minute (RPM):",
            options = listOf(
                "Speedometer",
                "Odometer",
                "Fuel gauge",
                "Tachometer"
            ),
            correctAnswerIndex = 3,
        ),
        Question(
            "What do you need to do before getting off the motorcycle?",
            options = listOf(
                "Turn off the engine immediately",
                "Properly place the side stand",
                "Shift to a higher gear",
                "Accelerate slightly to stabilize"
            ),
            correctAnswerIndex = 1,
        ),
        Question(
            "Before moving off, the rider must turn off the:",
            options = listOf(
                "Headlight",
                "Turn signal",
                "Choke",
                "Ignition"
            ),
            correctAnswerIndex = 2,
        ),
        Question("When the traffic light is steady green and steady left/right arrow:",
            imageResId = R.drawable.traffic_light_green_arrow,
        options = listOf(
            "Vehicles can go straight or vehicles on the right/left can make a left/right turn",
            "Vehicles can go left or right but not straight",
            "Vehicles can go straight but not left or right",
            "Vehicles cannot go straight or vehicles on the right/left cannot make a left/right turn"),
        correctAnswerIndex = 0
    ),
        // Pavement Markings
        Question("You are not allowed to cross a lane with continuous yellow line on the road except if you are:",
            imageResId = R.drawable.mark_yellow_center_line,
            options = listOf(
                "Changing lanes",
                "Turning left",
                "Turning right",
                "Overtaking"),
            correctAnswerIndex = 1
        ),
        Question("Broken yellow line on the road means:",
            imageResId = R.drawable.mark_broken_yellow_line,
            options = listOf(
                "Overtaking on the right side is allowed",
                "No Overtaking",
                "Overtaking on the left side is allowed",
                "No turning right"),
            correctAnswerIndex = 2
        ),
        Question("What does this road marking indicates:",
            imageResId = R.drawable.mark_bike_lane,
            options = listOf(
                "Motorcycle lane",
                "Bike lane",
                "Pedestrian lane",
                "Sidewalk"),
            correctAnswerIndex = 1
        ),
        // Road Sign Quizzes
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_pass_either_side,
            options = listOf(
                "Keep left or right",
                "Pass either side",
                "Divided highway ahead",
                "Give way"),
            correctAnswerIndex = 1
        ),
        Question("What does this road sign mean?",
            imageResId = R.drawable.sign_steep_descent,
            options = listOf(
                "Broken road",
                "Steep descent",
                "River",
                "Uneven road"),
            correctAnswerIndex = 1
        ),
        Question("What does this sign indicate?",
            imageResId = R.drawable.sign_merging_traffic_2,
            options = listOf(
                "Merging traffic approaching an intersection",
                "Approaching an intersection",
                "Approaching an intersection with a road on the side",
                "Merging traffic from the right"),
            correctAnswerIndex = 0
        ),
        Question("What should you do when you come across this sign?",
            imageResId = R.drawable.sign_slow_down,
            options = listOf(
                "Go faster and pray for safety",
                "Maintain maximum speed and be more alert than usual",
                "Take a U-turn",
                "Slow down and be more alert than usual"),
            correctAnswerIndex = 3
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_two_way,
            options = listOf(
                "Two-way traffic",
                "Divided highway",
                "No entry",
                "One way"),
            correctAnswerIndex = 0
        ),
        Question("What does this road sign mean?",
            imageResId = R.drawable.sign_no_uturn,
            options = listOf(
                "No right turn",
                "No left turn",
                "No U-turn",
                "No way"),
            correctAnswerIndex = 2
        )
    )
}
