package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
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
            questionTextView = findViewById(R.id.question_text_view)


            trueButton.setOnClickListener { view: View ->
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
               //println("quizViewModel.currentIndex  ==== ${quizViewModel.currentIndex}")
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

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        println("questionTextResId  ==== $questionTextResId")
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId: Int

        if(userAnswer == correctAnswer){
            messageResId = R.string.correct_toast
            quizViewModel.correctAnsweredQuestions++
        }
        else{
            messageResId = R.string.incorrect_toast
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