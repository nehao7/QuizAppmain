package com.daniel.quizbibz.fragments.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.databinding.FragmentQuizDifficultyLevelBinding
import com.daniel.quizbibz.utils.AppConsts
import com.daniel.quizbibz.utils.HelperFunctions


class QuizDifficultyLevelFragment : Fragment() {
    lateinit var binding: FragmentQuizDifficultyLevelBinding
    lateinit var mainActivity: MainActivity
    lateinit var helperFunctions: HelperFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentQuizDifficultyLevelBinding.inflate(layoutInflater)
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

        binding.tvQuizLevelEasy.setOnClickListener {
            mainActivity.navController.navigate(R.id.playQuizFragment,
                bundleOf(AppConsts.quizDifficulty to 1),helperFunctions.navOptions)
        }

        binding.tvQuizLevelMedium.setOnClickListener {
            mainActivity.navController.navigate(R.id.playQuizFragment,
                bundleOf(AppConsts.quizDifficulty to 2),helperFunctions.navOptions)
        }

        binding.tvQuizLevelHard.setOnClickListener {
            mainActivity.navController.navigate(R.id.playQuizFragment,
                bundleOf(AppConsts.quizDifficulty to 3),helperFunctions.navOptions)
        }

        binding.tvAddQuizQuestions.setOnClickListener {
            mainActivity.navController.navigate(R.id.allQuizFragment,null,helperFunctions.navOptions)
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
        fun newInstance(param1: String, param2: String) =
            QuizDifficultyLevelFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}