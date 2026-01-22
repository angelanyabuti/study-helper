package com.example.study_helper.flashcards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.study_helper.data.Flashcard
import com.example.study_helper.gemini.FlashcardUiState
import com.example.study_helper.gemini.FlashcardViewModel

@Composable
fun FlashcardScreen(viewModel: FlashcardViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is FlashcardUiState.Loading -> {
                CircularProgressIndicator()
            }
            is FlashcardUiState.Success -> {
                FlashcardContent(flashcards = state.flashcards)
            }
            is FlashcardUiState.Error -> {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
            is FlashcardUiState.Initial -> {
                Text(text = "Generate flashcards from the input screen to get started!")
            }
        }
    }
}

@Composable
fun FlashcardContent(flashcards: List<Flashcard>) {
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    val currentFlashcard = flashcards[currentCardIndex]

    // Reset flip state when the card changes
    remember(currentCardIndex) {
        isFlipped = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (isFlipped) currentFlashcard.answer else currentFlashcard.question,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { isFlipped = !isFlipped }) {
            Text(text = if (isFlipped) "Show Question" else "Show Answer")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { currentCardIndex = (currentCardIndex - 1 + flashcards.size) % flashcards.size },
                enabled = flashcards.size > 1
            ) {
                Text("Previous")
            }
            Text("${currentCardIndex + 1} / ${flashcards.size}")
            Button(
                onClick = { currentCardIndex = (currentCardIndex + 1) % flashcards.size },
                enabled = flashcards.size > 1
            ) {
                Text("Next")
            }
        }
    }
}
