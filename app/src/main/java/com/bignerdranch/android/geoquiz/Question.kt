package com.bignerdranch.android.geoquiz

import com.bignerdranch.android.geoquiz.StatusButtonPressed
import androidx.annotation.StringRes

enum class StatusButtonPressed{
    TRUE,
    FALSE,
    СHEAT,
    NOT_PRESSED
}
data class Question(@StringRes val textResId: Int, val answer: Boolean, var buttonPressed: StatusButtonPressed = StatusButtonPressed.NOT_PRESSED, var hasCheated: Boolean = false)