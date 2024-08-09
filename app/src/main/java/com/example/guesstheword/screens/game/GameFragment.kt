/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.guesstheword.screens.game

import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.guesstheword.R
import com.example.guesstheword.databinding.GameFragmentBinding
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import androidx.core.content.getSystemService

/**
 * Fragment where the game is played
 */
class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.game_fragment,
            container,
            false
        )

        Log.i("GameFragment", "Called ViewModelProvider!")
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

//        viewModel.score.observe(
//            viewLifecycleOwner,
//            Observer { newScore -> binding.scoreText.text = newScore.toString() })

//        viewModel.word.observe(
//            viewLifecycleOwner,
//            Observer { newWord -> binding.wordText.text = newWord.toString() })

//        viewModel.currentTime.observe(
//            viewLifecycleOwner,
//            Observer { newTimer -> binding.timerText.text = DateUtils.formatElapsedTime(newTimer) })

        viewModel.eventGameFinish.observe(viewLifecycleOwner, Observer { hasFinished ->
            if (hasFinished) {
                Log.i("GameFragment", "EventGameFinish Observer DO!")
                gameFinished()
                viewModel.onGameFinishComplete()
            }
        })

        viewModel.eventBuzz.observe(
            viewLifecycleOwner,
            Observer { buzzType ->
                if (buzzType != GameViewModel.BuzzType.NO_BUZZ) {
                    Log.i("GameFragment","BuzzType ${buzzType.pattern.joinToString()}")
                    buzz(buzzType.pattern)
                    viewModel.onBuzzComplete()
                }
            })

        return binding.root

    }


    /**
     * Called when the game is finished
     */
    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameToScore()
        action.setScore(viewModel.score.value ?: 0)
        findNavController().navigate(action)
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()
        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }


    }

}
