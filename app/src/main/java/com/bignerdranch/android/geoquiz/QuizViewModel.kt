package com.bignerdranch.android.geoquiz


import androidx.lifecycle.ViewModel
import java.util.*

private const val TAG = "QuizViewModel"
class QuizViewModel: ViewModel() {

    private val questionBank = LinkedList<Question>(listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas, true ),
        Question(R.string.question_asia,true)
    ))
    var totalAnsweredQuestions: Int = 0
    var correctAnsweredQuestions: Int = 0
    var currentIndex = 0
    var totalCheatTokens: Int = 3
        set(cheatToken: Int){
            if(field < 0){
                return
            }
            field -= cheatToken
        }


    var currentCheatState: Boolean
        get() = questionBank[currentIndex].hasCheated
        set(hasCheated: Boolean){
            questionBank[currentIndex].hasCheated = hasCheated
        }

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var currentButtonPressed: StatusButtonPressed
        get() = questionBank[currentIndex].buttonPressed
        set(buttonPressed: StatusButtonPressed){
            questionBank[currentIndex].buttonPressed = buttonPressed
        }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        if(currentIndex == 0) currentIndex = questionBank.size
        currentIndex = (currentIndex - 1) % questionBank.size
    }

}