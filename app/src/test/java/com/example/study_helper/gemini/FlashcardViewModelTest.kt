package com.example.study_helper.gemini

import com.example.study_helper.data.Flashcard
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FlashcardViewModelTest {

    //Creates a fake repository
    private val repository = mockk<FlashcardRepository>()
    private lateinit var vm: FlashcardViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        vm = FlashcardViewModel(repository)
    }

    @Test
    fun `success state when repository returns flashcards`() = runTest {
        // Arrange
        val flashcards = listOf(
            Flashcard("Q", "A")
        )

        //when someone asks for math cards, return the fake flashcards
        coEvery { repository.getFlashcards("math") } returns flashcards

        // Act
        // Starting the simulation
        vm.generateFlashcards("math")
        advanceUntilIdle() //when the internet takes time, this "fast forwards" the simulation time so the background work finishes fast.

        // Assert
        //checking the result
        val state = vm.uiState.value
        assert(state is FlashcardUiState.Success)
    }
}

