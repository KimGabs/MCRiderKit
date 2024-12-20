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

object DataSource {
    val menuList = listOf(
        "LTO Exam",
        "LTO Exam Reviewer",
        "Traffic Sign",
        "Hazard Perception Test"
    )

    val examTypeList = listOf(
        "Non-professional",
        "Professional"
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
        val question: String,
        val options: List<String>,
        val correctAnswerIndex:Int
    )

    val examQuestions = listOf(
        Question("The minimum age to get a non-professional license is:", listOf("15 years old", "17 years old", "18 years old", "19 years old"), 1),
        Question("Drivers gather most information using their:", listOf("Cars", "Computer", "Eyes", "Hands"), 2),
        Question("The safest speed of a vehicle is according to:", listOf("Road and weather conditions", "Capability of the vehicle", "Capability of the driver", "Capability of the passenger"), 0),
        Question("The deadline to renew the registration of a motor vehicle is:", listOf("At the end of the year", "At the end of the month", "At the discretion of the operator", "On the last working day of the month corresponding to the last digit of the motor vehicle plates"), 3),
        Question("How should you behave when an approaching officer flags down your vehicle?", listOf("Ignore the apprehending officer and drive away at increased speed", "Stop and argue with the apprehending officer", "Surrender your driver's license and other documents upon demand", "Bribe the apprehending officer to avoid fee"), 2),
        Question("To have one's driver's license suspended means to:", listOf("Have it revalidated by the LTO", "Have it taken away permanently by the LTO", "Have it taken temporarily by the LTO", "Have it renewed by the LTO"), 2)
    )

}
