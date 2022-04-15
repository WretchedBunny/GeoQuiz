package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.makeText
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = LinkedList<Question>(listOf(
         Question(R.string.question_australia,true, false, null),
         Question(R.string.question_oceans,true, false, null),
         Question(R.string.question_mideast,false, false, null),
         Question(R.string.question_africa,false, false, null),
         Question(R.string.question_americas, true, false, null),
         Question(R.string.question_asia,true, false, null)
     ))
    private val itr: ListIterator<Question> = questionBank.listIterator(questionBank.size)


    private var currentIndex = 0


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
                questionBank[currentIndex].buttonPressed = trueButton
                checkAnswer(true)
            }

            falseButton.setOnClickListener() { view: View ->
                questionBank[currentIndex].buttonPressed = falseButton
                checkAnswer(false)
            }
            nextButton.setOnClickListener(){
                currentIndex = (currentIndex + 1) % questionBank.size
                updateQuestion()
                isButtonPressed()
            }

            prevButton.setOnClickListener(){
                if(currentIndex == 0) currentIndex = questionBank.size
                currentIndex = (currentIndex - 1) % questionBank.size
                updateQuestion()
                isButtonPressed()

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
        }
        else{
            messageResId = R.string.incorrect_toast
        }
        makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun isButtonPressed(){
        val buttonPressed = questionBank[currentIndex].buttonPressed
        if(buttonPressed != null){
            disableButton(buttonPressed)
        }
        else{
            trueButton.isEnabled = true
            trueButton.isClickable = true
            falseButton.isClickable = true
            falseButton.isEnabled = true

        }
    }
    private fun disableButton(buttonPressed: Button){
        if(buttonPressed == trueButton){
            falseButton.isEnabled = false
            trueButton.isClickable = false

        }
        else if(buttonPressed == falseButton){
            trueButton.isEnabled = false
            falseButton.isClickable = false
        }
    }
}