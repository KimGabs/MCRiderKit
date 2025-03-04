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

import com.example.mcriderkit.R

object DataSource {
    val menuList = listOf(
        "LTO Exams",
        "Reviewer",
        "Hazard Perception Test"
    )

    val examList = listOf(
        "Student",
        "Non-professional",
        "Professional"
    )

    val examCategoryList = listOf(
        "Licensing Information",
        "Road Signs & Markings",
        "Traffic Rules & Regulations",
        "Traffic Violations & Penalties",
        "Vehicle Basics & Maintenance",
        "Driving Etiquette & Road Courtesy"
    )

    val licensingInfo = listOf(
        "Permits and Licenses",
        "Driver’s License Classification",
        "Qualifications and Documentary Requirements",
        "General Procedures in Securing Licenses and Permits"
    )

    val roadSignsAndMarkings = listOf(
        "Regulatory Signs",
        "Warning Signs",
        "Informative Signs",
        "Signs on Expressway",
        "Pavement Markings"
    )

    val PavementMarkings = listOf(
        "Longitudinal Lines",
        "Traversal Lines",
        "Other Lines"
    )

    data class Question(
        val question: String, // The text-based question
        val imageResId: Int? = null, // Optional image resource (null if no image)
        val options: List<String>, // List of answer options
        val correctAnswerIndex: Int, // Index of the correct answer
    )

    val examQuestions = listOf(
        // Licensing Information
        Question("What's the minimum age to get a non-professional license in the Philippines",
            options = listOf("15 years old", "17 years old", "18 years old", "19 years old"),
            correctAnswerIndex = 1,
        ),
        Question("Drivers gather most information using their:",
            options = listOf("Cars", "Computer", "Eyes", "Hands"),
            correctAnswerIndex = 2
        ),
        // Parking
        Question("Parking lights may be used:",
            options = listOf("During an emergency only", "At anytime", "For Parking and when visibility is poor", "When driving on a well-lighted streets"),
            correctAnswerIndex = 2
        ),
        Question("The safest speed of a vehicle is according to:",
            options = listOf("Road and weather conditions", "Capability of the vehicle", "Capability of the driver", "Capability of the passenger"),
            correctAnswerIndex = 0
        ),
        Question("What is the meaning of a blinking yellow traffic light?",
            options = listOf("Stop and stay until light stops flashing", "Wait for the green light", "Wait for the traffic enforcer", "Slow down and proceed with caution"),
            correctAnswerIndex = 3
        ),
        Question("How should you behave when an approaching officer flags down your vehicle?",
            options = listOf("Ignore the apprehending officer and drive away at increased speed", "Stop and argue with the apprehending officer", "Surrender your driver's license and other documents upon demand", "Bribe the apprehending officer to avoid fee"),
            correctAnswerIndex = 2
        ),
        Question("To have one's driver's license suspended means to:",
            options = listOf("Have it revalidated by the LTO", "Have it taken away permanently by the LTO", "Have it taken temporarily by the LTO", "Have it renewed by the LTO"),
            correctAnswerIndex = 2
        ),
        Question("What is the meaning of a traffic sign that has a triangle with red color?",
            options = listOf("Reminder Sign", "Crossroad sign", "Information sign", "Warning sign"),
            correctAnswerIndex = 3
        ),
        Question("The traffic light or signal that tells you to stop before the intersection is:",
            options = listOf("Steady red light", "Steady yellow light", "Steady green light", "Vehicle hazard lights"),
            correctAnswerIndex = 0
        ),
        Question("What is the meaning of a blinking red traffic light?",
            options = listOf("Go faster", "Stop at the given line", "Stop for a while and go if there is no danger ahead", "Stop 150 meters ahead"),
            correctAnswerIndex = 2
        ),
        Question("A driver of a vehicle approaching a yield sign in an intersection shall:",
            options = listOf("Go faster", "Go on without yielding to any vehicle in the intersection",
                "maintain speed and stop only when other vehicles blow their horn", "slow down and yield the right of way to any vehicle in the intersection"),
            correctAnswerIndex = 3
        ),
        Question("What is the meaning of the green traffic light?",
            options = listOf("Go faster", "A go signal for the vehicle",
                "Stop at given line", "slow down and yield to any approaching vehicle"),
            correctAnswerIndex = 1
        ),
        Question("Which of the following hand signal must a driver give when he wants to turn left?",
            options = listOf("Left arm bent at elbow, hand pointing up", "Left arm held down and hand pointing at the ground",
                "Left arm bent and hand pointing upwards", "Left arm held straight horizontally"),
            correctAnswerIndex = 3
        ),
        Question("When the traffic light is steady green and steady left/right arrow:",
            imageResId = R.drawable.traffic_light_green_arrow,
        options = listOf("vehicles can go straight or vehicles on the right/left can make a left/right turn", "Vehicles can go left or right but not straight",
            "Vehicles can go straight but not left or right", "Vehicles cannot go straight or vehicles on the right/left cannot make a left/right turn"),
        correctAnswerIndex = 0
    ),
        // Pavement Markings
        Question("You are not allowed to cross a lane with continuous yellow line on the road except if you are:",
            imageResId = R.drawable.mark_yellow_center_line,
            options = listOf("Changing lanes", "Turning left", "Turning right", "Overtaking"),
            correctAnswerIndex = 1
        ),
        Question("Broken yellow line on the road means:",
            imageResId = R.drawable.mark_broken_yellow_line,
            options = listOf("Overtaking on the right side is allowed", "No Overtaking", "Overtaking on the left side is allowed", "No turning right"),
            correctAnswerIndex = 2
        ),
        Question("A continuous yellow line on the road means:",
            imageResId = R.drawable.mark_yellow_center_line,
            options = listOf("Overtaking on the left side is allowed", "No Overtaking on either side", "No overtaking on the right side", "No overtaking on the left side"),
            correctAnswerIndex = 3
        ),
        Question("What does this road marking indicates:",
            imageResId = R.drawable.mark_bike_lane,
            options = listOf("Motorcycle lane", "Bike lane", "Pedestrian lane", "Sidewalk"),
            correctAnswerIndex = 1
        ),
        Question("You may not cross a single broken white or yellow line:",
            imageResId = R.drawable.mark_broken_yellow_line,
            options = listOf("When turning right into a driveway", "When turning left into a driveway", "When turning left into a drive thru", "When to do so would interfere with traffic"),
            correctAnswerIndex = 3
        ),
        Question("You can overtake on a highway with two lanes if there is a:",
            options = listOf("Bike lane", "Broken yellow line", "Continuous yellow line", "Continuous white line"),
            correctAnswerIndex = 1
        ),
        // Road Sign Quizzes
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_pass_either_side,
            options = listOf("Keep left or right", "Pass either side", "Divided highway ahead", "Give way"),
            correctAnswerIndex = 1
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_no_turns,
            options = listOf("No turns", "Give way", "One way", "Pass either side"),
            correctAnswerIndex = 0
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_keep_left,
            options = listOf("Two way", "Turn left", "Keep left", "One way"),
            correctAnswerIndex = 2
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_one_way,
            options = listOf("No turn", "Keep left", "Turn left", "One way"),
            correctAnswerIndex = 3
        ),
        Question("What does this road sign mean?",
            imageResId = R.drawable.sign_no_ped_cross,
            options = listOf("No pedestrian crossing", "No entry of pedestrians", "Pedestrian crossing", "No pedestrians Ahead"),
            correctAnswerIndex = 0
        ),
        Question("What does this road sign mean?",
            imageResId = R.drawable.sign_no_overtaking,
            options = listOf("No vehicles allowed", "No turning left", "No overtaking", "Pedestrians only"),
            correctAnswerIndex = 2
        ),
        Question("What does this sign indicate?",
            imageResId = R.drawable.sign_min_speed_limit,
            options = listOf("Minimum of 40 minutes on road", "Maintain minimum speed limit of 40 km/h", "Minimum of 40 vehicles only", "Minimum distance between vehicles"),
            correctAnswerIndex = 1
        ),
        Question("What does this sign mean?",
            imageResId = R.drawable.sign_roundabout,
            options = listOf("Take a Donut Drift", "Rotonda (roundabout) ahead", "Intersection ahead", "Spin Ahead"),
            correctAnswerIndex = 1
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_speed_limit,
            options = listOf("The distance to the next intersection is 60 km", "Minimum of 60 vehicles only", "Given distance between the vehicle", "Given Speed limit"),
            correctAnswerIndex = 3
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_traffic_light_ahead,
            options = listOf("Traffic signal ahead", "Railway crossing ahead", "No traffic light ahead", "Roundabout ahead"),
            correctAnswerIndex = 0
        ),
        Question("What does this sign indicate?",
            imageResId = R.drawable.sign_narrow_road,
            options = listOf("Narrow road ahead", "Divided highway ahead", "Railway crossing ahead", "Pass either side"),
            correctAnswerIndex = 0
        ),
        Question("What should you do when you see this sign?",
            imageResId = R.drawable.sign_stop,
            options = listOf("Stop only if other vehicles are approaching", "Stop sign 150 meters ahead", "Stop only near the drive thru", "Make a full stop at the intersection and proceed when the way is clear"),
            correctAnswerIndex = 3
        ),
        Question("What should you do when you see this sign?",
            imageResId = R.drawable.sign_no_parking,
            options = listOf("No entry", "No parking", "No use of horn", "No pedestrians allowed"),
            correctAnswerIndex = 1
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_curve_left,
            options = listOf("Dangerous curve on the left side", "Dangerous curve on the right side", "Intersection warning", "Turn left ahead"),
            correctAnswerIndex = 0
        ),
        Question("What does this road sign mean?",
            imageResId = R.drawable.sign_steep_descent,
            options = listOf("Broken road", "Steep descent", "River", "Uneven road"),
            correctAnswerIndex = 1
        ),
        Question("What does this sign indicate?",
            imageResId = R.drawable.sign_falling_debris,
            options = listOf("Slippery road", "Steep descent", "Falling debris", "Flood"),
            correctAnswerIndex = 2
        ),
        Question("What does this road sign indicate?",
            imageResId = R.drawable.sign_two_way,
            options = listOf("Two-way traffic", "Divided highway", "No entry", "One way"),
            correctAnswerIndex = 0
        ),
        Question("What does this road sign mean?",
            imageResId = R.drawable.sign_no_uturn,
            options = listOf("No right turn", "No left turn", "No U-turn", "No way"),
            correctAnswerIndex = 2
        ),
        Question("What does this sign indicate?",
            imageResId = R.drawable.sign_merging_traffic_2,
            options = listOf("Merging traffic approaching an intersection", "Approaching an intersection", "Approaching an intersection with a road on the side", "Merging traffic from the right"),
            correctAnswerIndex = 0
        ),
        Question("What should you do when you come across this sign?",
            imageResId = R.drawable.sign_slow_down,
            options = listOf("Go faster and pray for safety", "Maintain maximum speed and be more alert than usual", "Take a U-turn", "Slow down and be more alert than usual"),
            correctAnswerIndex = 3
        ),
    )

    val presetQuizScore = listOf(
        QuizScore(
            quizType = "Student Exam",
            highestScore = 8
        ),
        QuizScore(
            quizType = "Non-professional Exam",
            highestScore = 6
        ),
        QuizScore(
            quizType = "Professional Exam",
            highestScore = 10
        )
    )

    val presetHazardTests = listOf(
        HazardTest(
            title = "Test Video 1",
            location = "Location 1",
            speedLimit = "60 km/h",
            timeOfDay = "Morning",
            weather = "Clear",
            lastScore = 70,
            videoPath = "test1",
            thumbnailId = R.drawable.hazard_thumbnail_1,
            videoLength = 12,
            earlyRange = 0.61,
            perfectRange = 0.68,
            goodRange = 0.75,
            lateRange = 0.82
        ),
        HazardTest(
            title = "Test Video 2",
            location = "Location 2",
            speedLimit = "40 km/h",
            timeOfDay = "Afternoon",
            weather = "Clear",
            lastScore = 100,
            videoPath = "test2",
            thumbnailId = R.drawable.hazard_thumbnail_2,
            videoLength = 19,
            earlyRange = 0.31,
            perfectRange = 0.5,
            goodRange = 0.60,
            lateRange = 0.77
        ),
        HazardTest(
            title = "Test Video 3",
            location = "EDSA Highway",
            speedLimit = "30-40 km/h",
            timeOfDay = "Afternoon",
            weather = "Clear",
            lastScore = 50,
            videoPath = "test3",
            thumbnailId = R.drawable.hazard_thumbnail_3,
            videoLength = 30,
            earlyRange = 0.68,
            perfectRange = 0.72,
            goodRange = 0.76,
            lateRange = 0.80
        ),
        HazardTest(
            title = "Test Video 4",
            location = "Katipunan road",
            speedLimit = "20 km/h",
            timeOfDay = "Afternoon",
            weather = "Clear",
            lastScore = 90,
            videoPath = "test4",
            thumbnailId = R.drawable.hazard_thumbnail_4,
            videoLength = 28,
            earlyRange = 0.57,
            perfectRange = 0.62,
            goodRange = 0.64,
            lateRange = 0.66
        )
    )

    data class TutorialPage(
        val title: String,
        val text: String,
        val image: Int
    )

    val tutorialPages = listOf(
        // Hazard Perception Test Tutorial
        TutorialPage(
            title = "HazardTest",
            text = "Hazard Perception Test is designed to assess your ability to identify potential hazards on the road.",
            image = R.drawable.tutorial_hazard_1
            ),
        TutorialPage(
            title = "HazardTest",
            text = "As you watch the video, your task is to tap the on the gray area at the moment you spot a hazard developing. " +
                    "A red flag is shown to acknowledge your tap.",
            image = R.drawable.tutorial_hazard_2
        ),
    )

}
