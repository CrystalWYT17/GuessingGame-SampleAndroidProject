package com.example.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScore:Int ) : ViewModel() {

    private val _score = MutableLiveData<Int>()
    val score : LiveData<Int>
        get() = _score

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain : LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        Log.i("ScoreViewModel","Final score ${finalScore}")
        _score.value = finalScore
        _eventPlayAgain.value = false
    }

    fun onGamePlayAgain(){
        _eventPlayAgain.value = true
    }


}