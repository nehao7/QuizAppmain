package com.daniel.quizbibz.utils

import androidx.navigation.NavOptions

class HelperFunctions {
    init {
        fun String.capitalizeWords(): String {
            return this.split(" ").joinToString(" ") { it.capitalize() }
        }

    }

    // Define the custom animations

    // Define the custom animations
    val enterAnim: Int = android.R.anim.fade_in // Replace with your custom enter animation resource

    val exitAnim: Int = android.R.anim.fade_out // Replace with your custom exit animation resource

    // Create NavOptions with custom animations
    val navOptions: NavOptions = NavOptions.Builder()
        .setEnterAnim(enterAnim)
        .setExitAnim(exitAnim)
        .setPopEnterAnim(enterAnim)
        .setPopExitAnim(exitAnim)
        .build()

    fun convertTimeToMilliseconds(time: String?): Long {
        val parts = time?.split(":")
        if (parts?.size == 2) {
            try {
                val minutes = parts[0].toLong()
                val seconds = parts[1].toLong()
                return (minutes * 60 + seconds) * 1000 // Convert to milliseconds
            } catch (e: NumberFormatException) {
                // Handle the case where parsing fails
            }
        }
        return 0 // Return 0 if parsing fails or input is invalid
    }
}