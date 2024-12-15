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

    data class FlagQuestion(
        val flag: String,
        val options: List<String>,
        val correctAnswerIndex:Int
    )

    val examQuestions = listOf(
        FlagQuestion("🇺🇸", listOf("USA", "Canada", "Mexico", "Brazil"), 0),
        FlagQuestion("🇬🇧", listOf("Ireland", "Australia", "Canada", "UK"), 3),
        FlagQuestion("🇫🇷", listOf("Spain", "Italy", "France", "Portugal"), 2),
        FlagQuestion("🇯🇵", listOf("South Korea", "Japan", "China", "Thailand"), 1),
        FlagQuestion("🇮🇳", listOf("India", "Pakistan", "Bangladesh", "Sri Lanka"), 0),
    )

}
