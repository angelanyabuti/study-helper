package com.example.study_helper.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.study_helper.data.Flashcard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * listens to what the user wants and asks the repository to fetch the data that is needed
 * */

// Represents the different states of the UI
sealed interface FlashcardUiState {
    object Initial : FlashcardUiState
    object Loading : FlashcardUiState
    data class Success(val flashcards: List<Flashcard>) : FlashcardUiState
    data class Error(val message: String) : FlashcardUiState
}


//better to do dependency injection
class FlashcardViewModel(
    private val repository: FlashcardRepository = FlashcardRepository()
) : ViewModel() {
    // uiState is a StateFlow that holds the current state of the UI. What is the user seeing right now.
    // Private mutable state
    private val _uiState = MutableStateFlow<FlashcardUiState>(FlashcardUiState.Initial)

    // Public read-only state
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    fun generateFlashcards(topic: String) {
        //tells the app to do this task in the background
        viewModelScope.launch {
            _uiState.value = FlashcardUiState.Loading

            try {
                val flashcards = repository.getFlashcards(topic)
                _uiState.value = FlashcardUiState.Success(flashcards)
            } catch (e: Exception) {
                _uiState.value = FlashcardUiState.Error(
                    e.localizedMessage ?: "Failed to generate flashcards"
                )
            }
        }
    }
}
