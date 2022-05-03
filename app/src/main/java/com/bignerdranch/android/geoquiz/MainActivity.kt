package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.makeText
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private var  totalAnsweredQuestions: Int = 0
    private var correctAnsweredQuestions: Int = 0

    private val questionBank = LinkedList<Question>(listOf(
         Question(R.string.question_australia,true, null),
         Question(R.string.question_oceans,true, null),
         Question(R.string.question_mideast,false, null),
         Question(R.string.question_africa,false, null),
         Question(R.string.question_americas, true, null),
         Question(R.string.question_asia,true,  null)
    ))
    private val itr: ListIterator<Question> = questionBank.listIterator(questionBank.size)


    private var currentIndex = 0


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            Log.d(TAG,"onCreate(Bundle?) called")

            val provider: ViewModelProvider = ViewModelProviders.of(this)
            val quizViewModel = provider.get(QuizViewModel::class.java)
            Log.d(TAG,"Got a QuizViewModel: $quizViewModel")

            trueButton = findViewById(R.id.true_button)
            falseButton = findViewById(R.id.false_button)
            nextButton = findViewById(R.id.next_button)
            prevButton = findViewById(R.id.previous_button)
            questionTextView = findViewById(R.id.question_text_view)


            trueButton.setOnClickListener { view: View ->
                checkAnswer(true)
                disableButton(trueButton)
                totalAnsweredQuestions++
            }

            falseButton.setOnClickListener() { view: View ->
                checkAnswer(false)
                disableButton(falseButton)
                totalAnsweredQuestions++
            }
            nextButton.setOnClickListener(){
                currentIndex = (currentIndex + 1) % questionBank.size
                updateQuestion()
                disableButton(questionBank[currentIndex].buttonPressed)
                showPercentage(totalAnsweredQuestions, correctAnsweredQuestions)
            }

            prevButton.setOnClickListener(){
                if(currentIndex == 0) currentIndex = questionBank.size
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
                disableButton(questionBank[currentIndex].buttonPressed)
                showPercentage(totalAnsweredQuestions, correctAnsweredQuestions)
            }

            questionTextView.setOnClickListener(){
            currentIndex = currentIndex--
            updateQuestion()
        }

        val questionTextResId = questionBank[currentIndex].textResId
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
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }
    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId: Int

        if(userAnswer == correctAnswer){
            messageResId = R.string.correct_toast
            correctAnsweredQuestions++
        }
        else{
            messageResId = R.string.incorrect_toast
        }
        makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun disableButton(buttonWasPressed: Button?){
        when (buttonWasPressed) {
            trueButton -> {
                questionBank[currentIndex].buttonPressed = trueButton
                trueButton.isClickable = false
                trueButton.isEnabled = true
                falseButton.isEnabled = false
            }
            falseButton -> {
                questionBank[currentIndex].buttonPressed = falseButton
                falseButton.isClickable = false
                falseButton.isEnabled = true
                trueButton.isEnabled = false
            }
            else -> {
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