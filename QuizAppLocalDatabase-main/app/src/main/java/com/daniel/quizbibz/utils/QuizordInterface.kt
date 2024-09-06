package com.daniel.quizbibz.utils

import com.daniel.quizbibz.models.QuizDataClass
import com.daniel.quizbibz.models.WordDataClass

interface QuizInterface {
    fun deleteQuizItem(quizDataClass: QuizDataClass)
    fun updateQuizItem(position: Int)
}

interface WordInterface {
    fun deleteWordItem(wordDataClass: WordDataClass)

    fun updateWordItem(position: Int)
}