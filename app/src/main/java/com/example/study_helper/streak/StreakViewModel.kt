package com.example.study_helper.streak

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StreakViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = StreakRepository(application)

    private val _streakCount = MutableStateFlow(repository.getStreakCount())
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    /** Call when the user actually studies (e.g. flashcards are shown). */
    fun recordStudySession() {
        _streakCount.value = repository.recordStudySession()
    }
}
