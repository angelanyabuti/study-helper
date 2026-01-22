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

    suspend fun getFlashcards(topic: String): List<Flashcard> {
        val json = service.generateFlashcards(topic)
        return parser.parse(json)
    }
}
