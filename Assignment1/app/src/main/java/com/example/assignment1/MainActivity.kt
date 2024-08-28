package com.example.assignment1

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    // Variables to keep track of the score, the current wall hold, and if the climber has fallen
    private var score_display = 0
    private var currentWallHold = 0
    private var hasFallen = false

    // UI elements for displaying the score and handling user actions
    private lateinit var scoreTextView: TextView
    private lateinit var climbButton: Button
    private lateinit var fallButton: Button
    private lateinit var resetButton: Button
    private lateinit var scoreDisplay: TextView

    // Sets up the UI and restores the state if available
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.scoreTextView)
        climbButton = findViewById(R.id.climbButton)
        fallButton = findViewById(R.id.fallButton)
        resetButton = findViewById(R.id.resetButton)
        scoreDisplay = findViewById(R.id.scoreDisplay)

        if (savedInstanceState != null) {
            score_display = savedInstanceState.getInt("score_display")
            currentWallHold = savedInstanceState.getInt("currentWallHold")
            hasFallen = savedInstanceState.getBoolean("hasFallen")
            updateColorScoreText()
        }

        // Set up the click listeners for each button
        climbButton.setOnClickListener {
            if (!hasFallen) {
                climb()
            }
        }

        fallButton.setOnClickListener {
            if (currentWallHold > 0 && !hasFallen) {
                fall()
            }
        }

        resetButton.setOnClickListener {
            reset()
        }
    }

    // Method to handle the climbing action
    private fun climb() {
        if (currentWallHold < 9) {
            currentWallHold++
            Log.d("Assignment1", " Climb up, Current hold: $currentWallHold")

            when (currentWallHold) {
                in 1..3 -> score_display += 1
                in 4..6 -> score_display += 2
                in 7..9 -> score_display += 3
            }

            updateColorScoreText()
        }
    }

    // Method to handle the falling action
    private fun fall() {
        if (currentWallHold > 0) {
            currentWallHold--
            hasFallen = true
            Log.d("Assignment1", "Fall down, Current hold: $currentWallHold")

            if (currentWallHold < 9) {
                score_display = if (score_display >= 3) score_display - 3 else 0
            }

            updateColorScoreText()
        }
    }

    // Method to handle the resetting action
    private fun reset() {
        score_display = 0
        currentWallHold = 0
        hasFallen = false
        Log.d("Assignment1", "Score reset")
        updateColorScoreText()
    }

    // Determine color of the displayed score based on the current wall hold
    private fun Int.getScoreColor(): Int {
        return when (this) {
            in 1..3 -> ContextCompat.getColor(scoreDisplay.context, R.color.blue)
            in 4..6 -> ContextCompat.getColor(scoreDisplay.context, R.color.green)
            in 7..9 -> ContextCompat.getColor(scoreDisplay.context, R.color.red)
            else -> ContextCompat.getColor(scoreDisplay.context, R.color.black)
        }
    }

    // Update color of the displayed score based on the current wall hold
    private fun updateColorScoreText() {
        scoreDisplay.text = "$score_display"
        scoreDisplay.setTextColor(currentWallHold.getScoreColor())
    }

    // Save the state of the activity
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("score", score_display)
        outState.putInt("currentWallHold", currentWallHold)
        outState.putBoolean("hasFallen", hasFallen)
        super.onSaveInstanceState(outState)
    }

    // Restore the state of the activity
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        score_display = savedInstanceState.getInt("score_display")
        currentWallHold = savedInstanceState.getInt("currentWallHold")
        hasFallen = savedInstanceState.getBoolean("hasFallen")
        updateColorScoreText()
    }
}