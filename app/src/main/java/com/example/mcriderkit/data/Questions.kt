package com.example.mcriderkit.data

import com.google.firebase.firestore.DocumentId

data class Question(
    @DocumentId // 1. Put the annotation here
    val id: String = "",
    val text: String = "",
    val textTL: String = "",
    val choices: List<String> = emptyList(),
    val choicesTL: List<String> = emptyList(),
    val answerIndex: Int = 0,
    val imageUrl: String = "",
    val explanation: String? = null, // Use a nullable String
    val explanationTL: String? = null, // Use a nullable String
    val category: String = "" // Added this so you can filter by "Hazard", "Signs", etc.
)