package com.example.mcriderkit.data

import android.content.Context
import android.util.Log
import com.example.mcriderkit.lastEarnedTrophyName
import com.example.mcriderkit.showTrophyBanner
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.ceil

fun pushJsonToFirestore(context: Context) {
    val db = Firebase.firestore
    val gson = Gson()

    try {
        // Load the new file
        val jsonString = context.assets.open("hazardClips.json").bufferedReader().use { it.readText() }
        val listType = object : TypeToken<List<HazardClip>>() {}.type
        val clips: List<HazardClip> = gson.fromJson(jsonString, listType)

        val batch = db.batch()
        clips.forEach { clip ->
            // Use the 'id' from JSON as the Document ID in Firestore
            val docRef = db.collection("hazard_clips").document(clip.id)
            batch.set(docRef, clip)
        }

        batch.commit().addOnSuccessListener {
            Log.d("Firestore", "Successfully imported HPT clips!")
        }
    } catch (e: Exception) {
        Log.e("JSON_ERROR", "Error: ${e.message}")
    }
}

fun formatDate(timestamp: Long): String {
    // 1. Create a Date object from the milliseconds
    val date = Date(timestamp)

    // 2. Define the pattern (e.g., "MMM dd, yyyy" -> "Feb 11, 2026")
    val formatter = SimpleDateFormat("MMM dd, yyyy | hh:mm a", Locale.getDefault())

    // 3. Return the formatted string
    return formatter.format(date)
}

fun calculateDaysUntil(examTimestamp: Long): Long {
    if (examTimestamp == 0L) return 0

    val currentTime = System.currentTimeMillis()
    val difference = examTimestamp - currentTime

    // 1. Convert to Double first to keep the decimals
    // 2. Divide by milliseconds in a day
    // 3. Use ceil() to round up (e.g., 1.1 days becomes 2 days)
    val daysDouble = difference.toDouble() / (1000 * 60 * 60 * 24)

    val days = ceil(daysDouble).toLong()

    return if (days < 0) 0 else days
}

fun getMidnightTimestamp(): Long {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")) // Use UTC to avoid local offset bugs
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

fun checkAndAwardTrophies(userId: String, newStreak: Int, newPerfectCount: Int, newExamPassCount: Int ) {
    val userRef = FirebaseDatabase.getInstance().getReference("users/$userId")

    userRef.get().addOnSuccessListener { snapshot ->
        var currentTrophyCount = snapshot.child("trophyCount").getValue(Int::class.java) ?: 0

        // et separate milestones for both tracks
        val lastStreakMilestone = snapshot.child("lastStreakMilestone").getValue(Int::class.java) ?: 0
        val lastPrecisionMilestone = snapshot.child("lastPrecisionMilestone").getValue(Int::class.java) ?: 0
        val lastExamMilestone = snapshot.child("lastExamMilestone").getValue(Int::class.java) ?: 0

        // Determine if a new milestone was hit (e.g., 10, 20, or 30)
        val reachedStreakMilestone = when {
            newStreak >= 30 -> 30
            newStreak >= 20 -> 20
            newStreak >= 10 -> 10
            else -> 0
        }

        val reachedPrecisionMilestone = when {
            newPerfectCount >= 6 -> 6
            newPerfectCount >= 3 -> 3
            newPerfectCount >= 1 -> 1
            else -> 0
        }

        val reachedExam = when {
            newExamPassCount >= 7 -> 7
            newExamPassCount >= 3 -> 3
            newExamPassCount >= 1 -> 1
            else -> 0
        }

        val updates = mutableMapOf<String, Any>()
        var earnedNewTrophy = false

        // Check if a new Streak Trophy is earned
        if (reachedStreakMilestone > lastStreakMilestone) {
            currentTrophyCount++
            earnedNewTrophy = true
            updates["lastStreakMilestone"] = reachedStreakMilestone
        }

        // Check if a new Precision Trophy is earned
        if (reachedPrecisionMilestone > lastPrecisionMilestone) {
            currentTrophyCount++
            earnedNewTrophy = true
            updates["lastPrecisionMilestone"] = reachedPrecisionMilestone
        }
        if (reachedExam > lastExamMilestone) {
            currentTrophyCount++; earnedNewTrophy = true
            updates["lastExamMilestone"] = reachedExam
        }

        // 4. If any trophies were earned, update the database
        if (earnedNewTrophy) {
            updates["trophyCount"] = currentTrophyCount
            userRef.updateChildren(updates).addOnSuccessListener {
                lastEarnedTrophyName = "New Trophy Earned!" // You can customize this based on the milestone
                showTrophyBanner = true
            }
        }
    }
}

fun updateHazardStreakAndSave(
    userId: String,
    clipId: String,
    highestScore: Int,
    bestTapTime: Long,
    isDaily: Boolean,
) {
    val database = FirebaseDatabase.getInstance().getReference("users/$userId")

    // 1. Prepare Date Keys
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dateKey = sdf.format(Date())
    val todayMs = getMidnightTimestamp()
    val oneDayMs = 24 * 60 * 60 * 1000L

    // 2. Prepare the History Data
    val resultData = mapOf(
        "clipId" to clipId,
        "score" to highestScore,
        "date" to System.currentTimeMillis(),
        "dateKey" to dateKey,
        "Daily" to isDaily, // Matches @PropertyName("Daily")
        "tapTime" to bestTapTime
    )

    // 3. Save to History first
    database.child("hptHistory").push().setValue(resultData)

    database.get().addOnSuccessListener { snapshot ->
        val masteredClips = snapshot.child("masteredClips").children.mapNotNull { it.value as? String }.toMutableList()
        val lastHazardDate = snapshot.child("lastHazardDate").getValue(Long::class.java) ?: 0L
        val currentStreak = snapshot.child("streakCount").getValue(Int::class.java) ?: 0
        val maxStreak = snapshot.child("maxStreak").getValue(Int::class.java) ?: 0
        val examPassed = snapshot.child("examPassCount").getValue(Int::class.java) ?: 0
        val todayDateKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val isPassed = highestScore > 0
        val isPerfect = highestScore == 5

        val updates = mutableMapOf<String, Any>()

        // --- STREAK LOGIC (DAILY ONLY) ---
        if (isDaily) {
            // 🔥 MOVE THIS HERE so it's not blocked by the 'lastHazardDate' check
            val globalPath = "globalStats/$todayDateKey/$clipId/$highestScore"
            val globalRef = Firebase.database.getReference(globalPath)

            globalRef.runTransaction(object : Transaction.Handler {
                override fun doTransaction(mutableData: MutableData): Transaction.Result {
                    val currentValue = mutableData.getValue(Int::class.java) ?: 0
                    mutableData.value = currentValue + 1
                    return Transaction.success(mutableData)
                }

                override fun onComplete(
                    error: DatabaseError?,
                    committed: Boolean,
                    snapshot: DataSnapshot?
                ) {
                    if (error != null) Log.e("FIREBASE", "Global stats failed: ${error.message}")
                }
            })

            if (lastHazardDate != todayMs) {
                val newStreak = when {
                    !isPassed -> 0
                    todayMs - lastHazardDate == oneDayMs -> currentStreak + 1
                    else -> 1
                }
                val newMax = if (newStreak > maxStreak) newStreak else maxStreak

                updates["streakCount"] = newStreak
                updates["maxStreak"] = newMax
                updates["lastHazardDate"] = todayMs
                updates["lastHazardDateKey"] = dateKey
            }
        }


        // --- PERFECT SCORE LOGIC (PRACTICE ONLY) ---
        // 🚀 We only increment if isDaily is FALSE
        if (!isDaily && isPerfect && !masteredClips.contains(clipId)) {
            // 2. Add this clip to the unique mastered list
            masteredClips.add(clipId)
            updates["masteredClips"] = masteredClips

            // 3. The trophy count is now the size of the UNIQUE list
            val newPerfectCount = masteredClips.size
            updates["perfectScoreCount"] = newPerfectCount
        }

        // 4. Update Database & Check Trophies
        database.updateChildren(updates).addOnSuccessListener {
            val finalStreak = (updates["streakCount"] as? Int) ?: currentStreak
            val finalPerfects = (updates["perfectScoreCount"] as? Int) ?: masteredClips.size
            checkAndAwardTrophies(userId, finalStreak, finalPerfects, examPassed)
        }
    }
}
