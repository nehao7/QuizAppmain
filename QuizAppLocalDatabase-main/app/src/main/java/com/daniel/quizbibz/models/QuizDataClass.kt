package com.daniel.quizbibz.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizDataClass(
    @PrimaryKey(autoGenerate = true)
    var q_number: Int = 0,
    @ColumnInfo
    var question: String? = "",
    @ColumnInfo
    var option_a: String? = "",
    @ColumnInfo
    var option_b: String? = "",
    @ColumnInfo
    var option_c: String? = "",
    @ColumnInfo
    var option_d: String? = "",
    @ColumnInfo
    var correct_option: Int = 0,
    @ColumnInfo
    var questionType: Int? = 0,
    @ColumnInfo
    var questionTime: String? = ""

) {
    companion object {
        fun addQuestions(): List<QuizDataClass> {
            return mutableListOf(
                QuizDataClass(
                    question = "If you have six fish and half of them die from drowning, how many do you have?",
                    option_a = "Six",
                    option_b = "Two",
                    option_c = "Four",
                    option_d = "Three",
                    correct_option = 1,
                    questionType = 1,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "Take three houses. One is red, the other blue and the final one is white. The red house is to the left of the one in the middle. The blue house is right of the one in the middle. Where is the White House?",
                    option_a = "Left",
                    option_b = "Middle",
                    option_c = "Right",
                    option_d = "Washington D.C.",
                    correct_option = 4,
                    questionType = 1,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "You're sitting in a cabin in the dark and cold and you are hungry. You have only one match. What do you light first?",
                    option_a = "A fire to warm you up",
                    option_b = "Stove",
                    option_c = "Matchstick",
                    option_d = "None",
                    correct_option = 3,
                    questionType = 1,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "Clive's dad had five sons. He named them 1, 2, 3, 4 and _______.",
                    option_a = "6",
                    option_b = "5",
                    option_c = "9",
                    option_d = "Clive",
                    correct_option = 4,
                    questionType = 1,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "If 10 pigeons sat on a wire and someone shot one, how many would be left?",
                    option_a = "10 Maybe missed",
                    option_b = "Nine",
                    option_c = "Poor pigeons",
                    option_d = "0",
                    correct_option = 4,
                    questionType = 2,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "A house takes eight hours to be built by four men. How much time would it take one man to build it?",
                    option_a = "32 Hours",
                    option_b = "18 Hours",
                    option_c = "20 Hours",
                    option_d = "Zero",
                    correct_option = 4,
                    questionType = 2,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "What did the eye say to the other eye?",
                    option_a = "You Are An Eyesore",
                    option_b = "Between Us something Smells",
                    option_c = "Are You Looking At Me",
                    option_d = "Aye Aye!",
                    correct_option = 2,
                    questionType = 3,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "When you dig a hole 4 feet deep, 5 feet wide and 6 feet long, how much dirt is in it?",
                    option_a = "100 pound",
                    option_b = "1 ton",
                    option_c = "300 pound",
                    option_d = "None",
                    correct_option = 4,
                    questionType = 3,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "While the wind is blowing from east to west, an orange falls from a tree. Where will it land?",
                    option_a = "At the bottom of the hill",
                    option_b = "East of tree",
                    option_c = "West of tree",
                    option_d = "On the ground",
                    correct_option = 4,
                    questionType = 2,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "Heavy it is but reverse it's not?",
                    option_a = "Ton",
                    option_b = "Metal",
                    option_c = "Piles of Wood",
                    option_d = "Car",
                    correct_option = 1,
                    questionType = 2,
                    questionTime = "12",
                ),
                QuizDataClass(
                    question = "In a year, how many seconds are there?",
                    option_a = "6",
                    option_b = "35",
                    option_c = "12",
                    option_d = "9",
                    correct_option = 3,
                    questionType = 2,
                    questionTime = "20",
                ),
                QuizDataClass(
                    question = "A doctor gives you three pills with the strict instruction to take one every half hour. How long will it take to finish them?",
                    option_a = "One Hours",
                    option_b = "Two Hour",
                    option_c = "Half an Hour",
                    option_d = "One and a Half Hour",
                    correct_option = 1,
                    questionType = 2,
                    questionTime = "20",
                ),
            )
        }
    }
}

@Entity
data class WordDataClass(
    @PrimaryKey(autoGenerate = true)
    var q_num: Int = 0,
    @ColumnInfo
    var answer: String = "",
    @ColumnInfo
    var type: Int? = 0
) {
    companion object {
        fun addWordQuestions(): List<WordDataClass> {
            return mutableListOf(
                WordDataClass(answer = "food", type = 1),
                WordDataClass(answer = "cake", type = 1),
                WordDataClass(answer = "chair", type = 1),
                WordDataClass(answer = "mobile", type = 1),
                WordDataClass(answer = "teddy", type = 1),
                WordDataClass(answer = "word", type = 1),
                WordDataClass(answer = "class", type = 1),
                WordDataClass(answer = "pencil", type = 1),
                WordDataClass(answer = "bread", type = 1),
                WordDataClass(answer = "drink", type = 1),
                WordDataClass(answer = "quiz", type = 1),
                WordDataClass(answer = "butter", type = 1),
                WordDataClass(answer = "butterfly", type = 2),
                WordDataClass(answer = "biscuit", type = 2),
                WordDataClass(answer = "corns", type = 2),
                WordDataClass(answer = "medium", type = 2),
                WordDataClass(answer = "project", type = 2),
                WordDataClass(answer = "wishlist", type = 2),
                WordDataClass(answer = "classmate", type = 2),
                WordDataClass(answer = "answer", type = 2),
                WordDataClass(answer = "pillow", type = 2),
                WordDataClass(answer = "drone", type = 2),
                WordDataClass(answer = "needle", type = 2),
                WordDataClass(answer = "vegetables", type = 2),
                WordDataClass(answer = "difficult", type = 3),
                WordDataClass(answer = "construct", type = 3),
                WordDataClass(answer = "triumph", type = 3),
                WordDataClass(answer = "mistake", type = 3),
                WordDataClass(answer = "positive", type = 3),
                WordDataClass(answer = "stake", type = 3),
                WordDataClass(answer = "upgrade", type = 3),
                WordDataClass(answer = "sweat", type = 3),
                WordDataClass(answer = "window", type = 3),
                WordDataClass(answer = "quilt", type = 3),
                WordDataClass(answer = "technology", type = 3),
                WordDataClass(answer = "toothpick", type = 3)
            )
        }
    }
}