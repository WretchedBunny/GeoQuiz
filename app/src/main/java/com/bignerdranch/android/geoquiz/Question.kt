package com.bignerdranch.android.geoquiz

import android.widget.Button
import androidx.annotation.StringRes

data class Question(@StringRes val textResId: Int, val answer: Boolean, var isAnswered: Boolean = false, var buttonPressed: Button?)