package com.daniel.quizbibz.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.daniel.quizbibz.R
import com.daniel.quizbibz.database.QuizDatabase


class SplashActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var quizDatabase: QuizDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        quizDatabase = QuizDatabase.createDatabase(this)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },5000)




    }
}