package com.bignerdranch.android.geoquiz

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Button

private const val EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true"
private const val TAG = "CheatActivity"
const val EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown"
const val EXTRA_BUTTON_PRESSED = "com.bignerdranch.andorid.geoquiz.extra_button_pressed"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerTextView = findViewById(R.id.answer_tex_view)
        showAnswerButton = findViewById(R.id.show_answer_button)

        showAnswerButton.setOnClickListener {

            when(answerIsTrue){
                true -> {
                    answerTextView.setText(R.string.true_button)
                    setAnswerShownResult(true, StatusButtonPressed.TRUE)
                }
                false -> {
                    answerTextView.setText(R.string.false_button)
                    setAnswerShownResult(false, StatusButtonPressed.FALSE)
                }
            }
        }


        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
    }

    companion object {
       fun newIntent(packageContext: Context,  answerIsTrue: Boolean): Intent {
           return Intent(packageContext, CheatActivity::class.java).apply {
               putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
           }
       }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean, buttonPressed: StatusButtonPressed){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_BUTTON_PRESSED, buttonPressed)
        }
        setResult(0, data)
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
}