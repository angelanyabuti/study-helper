package com.example.study_helper.gemini

import com.example.study_helper.data.Flashcard

/**
 * This calls the raw data from the Gemini Service
 * It is separate in case you want to use a different AI you only change this file
 * **/
class FlashcardRepository(
    private val service: GeminiService = GeminiService(),
    private val parser: FlashcardParser = FlashcardParser()
) {

    suspend fun getFlashcards(topic: String, count: Int = 5, difficulty: String = "Beginner"): List<Flashcard> {
        try {
            val json = service.generateFlashcards(topic, count, difficulty)
            println("Raw JSON from service: $json")  // Debug log
            return parser.parse(json)
        } catch (e: Exception) {
            e.printStackTrace()  // Shows full error in Logcat
            throw e  // Re-throw to be caught by ViewModel
        }
    }
}
