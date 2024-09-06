package com.daniel.quizbibz.database

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daniel.quizbibz.R
import com.daniel.quizbibz.models.QuizDataClass
import com.daniel.quizbibz.models.WordDataClass

@Database(version = 2, entities = [(QuizDataClass::class), (WordDataClass::class)])
abstract class
QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao

    companion object {
        private var quizDatabase: QuizDatabase? = null

       /* val migration1to2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Define your SQL statements to update the schema from version 1 to 2
                // For example, you can use SQL ALTER TABLE statements
                database.execSQL("ALTER TABLE QuizDataClass ADD COLUMN questionTime TEXT")
            }
        }*/


        fun createDatabase(context: Context): QuizDatabase {
            if (quizDatabase == null) {
                quizDatabase = Room.databaseBuilder(
                    context,
                    QuizDatabase::class.java,
                    context.resources.getString(R.string.app_name)
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            class insertQuestion : AsyncTask<Void, Void, Void>() {
                                override fun doInBackground(vararg params: Void?): Void? {
                                    createDatabase(context).quizDao()
                                        .insertAllQuestions(QuizDataClass.addQuestions())
                                    createDatabase(context).quizDao()
                                        .insertAllWordQuestion(WordDataClass.addWordQuestions())
                                    return null
                                }
                            }
                            insertQuestion().execute()
                        }
                    }
                    )
                  //  .addMigrations(migration1to2)
                    .build()
            }
            return quizDatabase!!
        }

    }

}