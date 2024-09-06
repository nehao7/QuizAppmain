package com.daniel.quizbibz.fragments.word_puzzle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.databinding.FragmentWordPuzzleDifficultyLevelBinding
import com.daniel.quizbibz.utils.AppConsts
import com.daniel.quizbibz.utils.HelperFunctions


class WordPuzzleDifficultyLevelFragment : Fragment() {
    lateinit var binding: FragmentWordPuzzleDifficultyLevelBinding
    lateinit var mainActivity: MainActivity
    lateinit var helperFunctions: HelperFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentWordPuzzleDifficultyLevelBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        helperFunctions = HelperFunctions()
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvWordLevelEasy.setOnClickListener {
            mainActivity.navController.navigate(
                R.id.playWordPuzzleFragment,
                bundleOf(AppConsts.quizDifficulty to 1), helperFunctions.navOptions
            )
        }

        binding.tvWordLevelMedium.setOnClickListener {
            mainActivity.navController.navigate(
                R.id.playWordPuzzleFragment,
                bundleOf(AppConsts.quizDifficulty to 2),
                helperFunctions.navOptions
            )
        }

        binding.tvWordLevelHard.setOnClickListener {
            mainActivity.navController.navigate(
                R.id.playWordPuzzleFragment,
                bundleOf(AppConsts.quizDifficulty to 3),
                helperFunctions.navOptions
            )
        }

        binding.tvAddWord.setOnClickListener {
            mainActivity.navController.navigate(
                R.id.allWordPuzzleFragment,
                null,
                helperFunctions.navOptions
            )
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizDifficultyLevelFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            WordPuzzleDifficultyLevelFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}