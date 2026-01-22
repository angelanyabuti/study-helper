package com.example.study_helper.gemini

import com.example.study_helper.data.Flashcard
import org.json.JSONArray

class FlashcardParser {

    fun parse(jsonString: String): List<Flashcard> {
        val cleaned = jsonString.trim()
            .removePrefix("```json")
            .removeSuffix("```")
            .trim()

        val jsonArray = JSONArray(cleaned)
        val flashcards = mutableListOf<Flashcard>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            flashcards.add(
                Flashcard(
                    question = obj.getString("question"),
                    answer = obj.getString("answer")
                )
            )
        }

        return flashcards
    }
}
