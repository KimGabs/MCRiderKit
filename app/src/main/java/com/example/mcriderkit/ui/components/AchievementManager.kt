package com.example.mcriderkit.ui.components

import android.content.Context
import android.content.SharedPreferences

class AchievementManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_achievements", Context.MODE_PRIVATE)

    fun setAchievementShown() {
        sharedPreferences.edit().putBoolean("trophy_shown", true).apply()
    }

    fun isAchievementShown(): Boolean {
        return sharedPreferences.getBoolean("trophy_shown", false)
    }
}
