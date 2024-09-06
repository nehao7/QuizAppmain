package com.daniel.quizbibz.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.daniel.quizbibz.models.QuizDataClass
import com.daniel.quizbibz.models.WordDataClass

@Dao
interface QuizDao {
    @Insert
    fun insertIntoQuiz(quizDataClass: QuizDataClass): Long

    @Update
    fun updateQuestion(quizDataClass: QuizDataClass)

    @Query("SELECT * FROM QuizDataClass ")
    fun getQuestionList(): List<QuizDataClass>

    @Query("SELECT * FROM QuizDataClass WHERE questionType=:questionType")
    fun getQuizQuestionList(questionType: Int): List<QuizDataClass>

    @Update
    fun updateWordQuestion(quizDataClass: WordDataClass)

    @Delete
    fun deleteQuestion(quizDataClass: QuizDataClass)

    @Insert
    fun insertAllQuestions(questions: List<QuizDataClass>)

    @Insert
    fun insertIntoWord(wordDataClass: WordDataClass): Long

    @Insert
    fun insertAllWordQuestion(addquestion: List<WordDataClass>)

    @Query("SELECT * FROM WordDataClass ")
    fun getWordList(): List<WordDataClass>

    @Query("SELECT * FROM WordDataClass WHERE type=:difficultyLevel")
    fun getWordPuzzleList(difficultyLevel:Int?): List<WordDataClass>

    @Delete
    fun deleteWordQuestion(wordDataClass: WordDataClass)


}