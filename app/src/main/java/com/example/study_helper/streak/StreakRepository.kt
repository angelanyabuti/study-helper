package com.example.study_helper.streak

import android.content.Context

/**
 * Persists the user's study streak locally. A streak survives if the user studies
 * on consecutive calendar days; skipping a day resets it back to 1.
 */
class StreakRepository(context: Context) {
    private val prefs = context.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)

    private fun todayEpochDay(): Long = System.currentTimeMillis() / MILLIS_PER_DAY

    fun getStreakCount(): Int {
        val lastDay = prefs.getLong(KEY_LAST_STUDY_DAY, -1L)
        if (lastDay == -1L) return 0
        val daysSinceLastStudy = todayEpochDay() - lastDay
        return if (daysSinceLastStudy > 1) 0 else prefs.getInt(KEY_STREAK_COUNT, 0)
    }

    /** Call once per study session. Returns the streak count after recording it. */
    fun recordStudySession(): Int {
        val today = todayEpochDay()
        val lastDay = prefs.getLong(KEY_LAST_STUDY_DAY, -1L)
        val currentStreak = prefs.getInt(KEY_STREAK_COUNT, 0)

        val newStreak = when (today - lastDay) {
            0L -> currentStreak
            1L -> currentStreak + 1
            else -> 1
        }

        prefs.edit()
            .putInt(KEY_STREAK_COUNT, newStreak)
            .putLong(KEY_LAST_STUDY_DAY, today)
            .apply()

        return newStreak
    }

    private companion object {
        const val KEY_STREAK_COUNT = "streak_count"
        const val KEY_LAST_STUDY_DAY = "last_study_day"
        const val MILLIS_PER_DAY = 24L * 60 * 60 * 1000
    }
}
