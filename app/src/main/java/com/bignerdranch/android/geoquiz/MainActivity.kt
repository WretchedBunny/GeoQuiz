package com.bignerdranch.android.geoquiz

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val TAG_CHEATER = "Cheate state"
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

        @SuppressLint("RestrictedApi")
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
                checkCheater()
            }

            prevButton.setOnClickListener{
                quizViewModel.moveToPrev()
                //println("quizViewModel.currentIndex  ==== ${quizViewModel.currentIndex}")
                updateQuestion()
                disableButton(quizViewModel.currentButtonPressed)
                showPercentage(quizViewModel.totalAnsweredQuestions, quizViewModel.correctAnsweredQuestions)
                checkCheater()
            }


            cheatButton.setOnClickListener(){ view ->
                //start cheatActivity
                //Log.d(TAG,"AAAAAAAAAAAAA ${quizViewModel.totalCheatTokens}")

                disableButton(StatusButtonPressed.СHEAT)
                val correctAnswer = quizViewModel.currentQuestionAnswer
                val intent = CheatActivity.newIntent(this,correctAnswer)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val options = ActivityOptions.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                    startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
                } else {
                    startActivityForResult(intent, REQUEST_CODE_CHEAT)
                }
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
        disableButton(StatusButtonPressed.СHEAT)
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
            quizViewModel.currentCheatState = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            quizViewModel.currentButtonPressed = (data?.getSerializableExtra(EXTRA_BUTTON_PRESSED) ?: StatusButtonPressed.NOT_PRESSED) as StatusButtonPressed
            quizViewModel.totalCheatTokens = 1
            Log.d(TAG, "onActivityResult(), RequestCodeCheat called")
        }
    }

    private fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId: Int = when (userAnswer) {
            correctAnswer -> {
                quizViewModel.correctAnsweredQuestions++
                R.string.correct_toast
            }
            else -> {R.string.incorrect_toast}
        }
        makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun checkCheater(){
        Log.d(TAG_CHEATER, "${quizViewModel.currentCheatState} Current index: ${quizViewModel.currentIndex}")
        val isCheater = quizViewModel.currentCheatState
        if(isCheater){
            makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun disableButton(buttonWasPressed: StatusButtonPressed){
        when (buttonWasPressed) {
            StatusButtonPressed.TRUE -> {
                quizViewModel.currentButtonPressed = StatusButtonPressed.TRUE
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
            StatusButtonPressed.СHEAT -> {
                if(quizViewModel.totalCheatTokens <= 0) {
                    cheatButton.isEnabled = false
                    cheatButton.isClickable = true
                    makeText(this, R.string.no_cheat_tokens_left, Toast.LENGTH_SHORT).show()
                }
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