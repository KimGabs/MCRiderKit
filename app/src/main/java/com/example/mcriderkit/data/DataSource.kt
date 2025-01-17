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
        "Traffic Sign",
        "Hazard Perception Test"
    )

    val examList = listOf(
        "Non-professional",
        "Professional",
        "Road Sign"
    )

    val reviewerMenu = listOf(
        "Reviewer"
    )

    val examCategoryList = listOf(
        "All Questions",
        "Emergencies",
        "General Knowledge",
        "Handling & Driving",
        "Parking",
        "Read Position",
        "Signs & Markings",
        "Violations & Penalties"
    )

    data class Question(
        val question: String, // The text-based question
        val imageResId: Int? = null, // Optional image resource (null if no image)
        val options: List<String>, // List of answer options
        val correctAnswerIndex: Int // Index of the correct answer
    )


    val examQuestions = listOf(
        Question("The minimum age to get a non-professional license is:",
            options = listOf("15 years old", "17 years old", "18 years old", "19 years old"),
            correctAnswerIndex = 1
        ),
        Question("Drivers gather most information using their:",
            options = listOf("Cars", "Computer", "Eyes", "Hands"),
            correctAnswerIndex = 2
        ),
        Question("The safest speed of a vehicle is according to:",
            options = listOf("Road and weather conditions", "Capability of the vehicle", "Capability of the driver", "Capability of the passenger"),
            correctAnswerIndex = 0
        ),
        Question("The deadline to renew the registration of a motor vehicle is:",
            options = listOf("At the end of the year", "At the end of the month", "At the discretion of the operator", "On the last working day of the month corresponding to the last digit of the motor vehicle plates"),
            correctAnswerIndex = 3
        ),
        Question("How should you behave when an approaching officer flags down your vehicle?",
            options = listOf("Ignore the apprehending officer and drive away at increased speed", "Stop and argue with the apprehending officer", "Surrender your driver's license and other documents upon demand", "Bribe the apprehending officer to avoid fee"),
            correctAnswerIndex = 2
        ),
        Question("To have one's driver's license suspended means to:",
            options = listOf("Have it revalidated by the LTO", "Have it taken away permanently by the LTO", "Have it taken temporarily by the LTO", "Have it renewed by the LTO"),
            correctAnswerIndex = 2
        )
    )

    val presetHazardTests = listOf(
        HazardTest(
            title = "Test Video 1",
            location = "Location 1",
            speedLimit = "60 km/h",
            timeOfDay = "Morning",
            weather = "Clear",
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
            videoPath = "test2",
            thumbnailId = R.drawable.hazard_thumbnail_2,
            videoLength = 19,
            earlyRange = 0.31,
            perfectRange = 0.5,
            goodRange = 0.60,
            lateRange = 0.77
        )
    )

    data class QuestionImage(
        val questionImage: Int,
        val options: List<String>,
        val correctAnswerIndex: Int
    )
}
