package com.daniel.quizbibz.activities

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daniel.quizbibz.R
import com.daniel.quizbibz.database.QuizDatabase
import com.daniel.quizbibz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var quizDatabase: QuizDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        quizDatabase = QuizDatabase.createDatabase(this)

        navController = findNavController(R.id.fragment)

        // Check if auto-rotation is enabled
        val autoRotate = Settings.System.getInt(
            contentResolver,
            Settings.System.ACCELEROMETER_ROTATION, 0
        ) == 1

        // Set the appropriate orientation
        requestedOrientation = if (autoRotate) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}





