package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"

private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView



    private val quizViewModel: QuizViewModel by lazy{
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            Log.d(TAG,"onCreate(Bundle?) called")

            trueButton = findViewById(R.id.true_button)
            falseButton = findViewById(R.id.false_button)
            nextButton = findViewById(R.id.next_button)
            prevButton = findViewById(R.id.previous_button)
            cheatButton = findViewById(R.id.cheat_button)
            questionTextView = findViewById(R.id.question_text_view)


            trueButton.setOnClickListener {
                checkAnswer(true)
                disableButton(StatusButtonPressed.TRUE)
                quizViewModel.totalAnsweredQuestions++
            }

            falseButton.setOnClickListener { view: View ->
                checkAnswer(false)
                disableButton(StatusButtonPressed.FALSE)
                quizViewModel.totalAnsweredQuestions++
            }
            nextButton.setOnClickListener{
                quizViewModel.moveToNext()
                updateQuestion()
                disableButton(quizViewModel.currentButtonPressed)
                showPercentage(quizViewModel.totalAnsweredQuestions, quizViewModel.correctAnsweredQuestions)
            }

            prevButton.setOnClickListener{
                quizViewModel.moveToPrev()
                //println("quizViewModel.currentIndex  ==== ${quizViewModel.currentIndex}")
                updateQuestion()
                disableButton(quizViewModel.currentButtonPressed)
                showPercentage(quizViewModel.totalAnsweredQuestions, quizViewModel.correctAnsweredQuestions)
            }

            cheatButton.setOnClickListener(){
                //start cheatActivity
                val correctAnswer = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this,correctAnswer)
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }

            questionTextView.setOnClickListener{
            quizViewModel.currentIndex--
            updateQuestion()
        }
            val questionTextResId = quizViewModel.currentQuestionText
            questionTextView.setText(questionTextResId)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
        disableButton(quizViewModel.currentButtonPressed)
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG,"onResume() called")
    }


    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult() called")

        if(resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult(), RESULT_OK called")
            return
        }

        if(resultCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.currentButtonPressed = data?.getSerializableExtra(EXTRA_BUTTON_PRESSED) as StatusButtonPressed
            Log.d(TAG, "onActivityResult(), RequestCodeCheat called")
        }
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId: Int = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                quizViewModel.correctAnsweredQuestions++
                R.string.correct_toast
            }
            else ->
                R.string.incorrect_toast
        }
        makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun disableButton(buttonWasPressed: StatusButtonPressed){
        when (buttonWasPressed) {
            StatusButtonPressed.TRUE -> {
                quizViewModel.currentButtonPressed = StatusButtonPressed.TRUE //questionBank[currentIndex].buttonPressed = trueButton
                trueButton.isClickable = false
                trueButton.isEnabled = true
                falseButton.isEnabled = false
            }
            StatusButtonPressed.FALSE -> {
                quizViewModel.currentButtonPressed = StatusButtonPressed.FALSE
                falseButton.isClickable = false
                falseButton.isEnabled = true
                trueButton.isEnabled = false
            }
            StatusButtonPressed.NOT_PRESSED -> {
                trueButton.isEnabled = true
                trueButton.isClickable = true
                falseButton.isEnabled = true
                falseButton.isClickable = true
            }
        }
    }
    private fun showPercentage(totalAnswered: Int, correctAnsweredQuestions: Int){
        if(totalAnswered == 6){
            val percentage: Float = ((correctAnsweredQuestions * 100) / totalAnswered).toFloat()
            makeText(this, "You answered $percentage%\nGood Job!", Toast.LENGTH_SHORT).show()
        }
    }

}