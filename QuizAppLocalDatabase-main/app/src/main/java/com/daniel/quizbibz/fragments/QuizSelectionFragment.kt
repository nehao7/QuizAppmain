package com.daniel.quizbibz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.databinding.FragmentQuizSelectionBinding
import com.daniel.quizbibz.utils.HelperFunctions


class QuizSelectionFragment : Fragment() {
    lateinit var binding: FragmentQuizSelectionBinding
    lateinit var mainactivity: MainActivity
    lateinit var helperFunctions: HelperFunctions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentQuizSelectionBinding.inflate(layoutInflater)
        mainactivity = activity as MainActivity
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

        binding.tvQuizGame.setOnClickListener {
            mainactivity.navController.navigate(R.id.quizDifficultyLevelFragment,null,helperFunctions.navOptions)
        }

        binding.tvWordPuzzleGame.setOnClickListener {
            mainactivity.navController.navigate(R.id.wordPuzzleDifficultyLevelFragment,null,helperFunctions.navOptions)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizSelectionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            QuizSelectionFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}