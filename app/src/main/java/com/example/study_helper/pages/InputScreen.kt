package com.example.study_helper.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.study_helper.gemini.FlashcardUiState
import com.example.study_helper.gemini.FlashcardViewModel

@Composable
fun InputScreen(navController: NavController, viewModel: FlashcardViewModel) {
    var topic by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // This LaunchedEffect will listen for the Success state and navigate when it occurs.
    LaunchedEffect(uiState) {
        if (uiState is FlashcardUiState.Success) {
            navController.navigate("flashcards")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is FlashcardUiState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Generating flashcards, please wait...")
            }
            is FlashcardUiState.Error -> {
                Text("An error occurred. Please try again.", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.generateFlashcards(topic) }) {
                    Text("Retry")
                }
            }
            else -> {
                Text("What topic do you want to study?", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = topic,
                    onValueChange = { topic = it },
                    label = { Text("Enter a topic (e.g., 'The Roman Empire')") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState !is FlashcardUiState.Loading
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.generateFlashcards(topic) },
                    enabled = topic.isNotBlank() && uiState !is FlashcardUiState.Loading
                ) {
                    Text("Generate Flashcards")
                }
            }
        }
    }
}
