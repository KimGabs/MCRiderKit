package com.example.mcriderkit.data

import com.google.firebase.database.PropertyName

data class ExamResult(
    val score: Int = 0,
    val total: Int = 0,
    val percentage: Int = 0,
    val date: Long = 0L,
    val examType: String = "",
    val passed: Boolean = false
)

data class HptResult(
    val clipId: String = "",
    val date: Long = 0L,
    val dateKey: String = "",

    // 🔥 Tell Firebase to look for "Daily" even if the variable is named isDaily
    @get:PropertyName("Daily")
    @set:PropertyName("Daily")
    var isDaily: Boolean = false,

    val score: Int = 0,
    val tapTime: Long = 0,
)