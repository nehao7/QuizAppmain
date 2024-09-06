package com.daniel.quizbibz.fragments.word_puzzle

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.database.QuizDatabase
import com.daniel.quizbibz.databinding.FragmentPlayWordPuzzleBinding
import com.daniel.quizbibz.models.WordDataClass
import com.daniel.quizbibz.utils.AppConsts
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.Random
import java.util.concurrent.TimeUnit


class PlayWordPuzzleFragment : Fragment() {
    var wordarray = arrayListOf<WordDataClass>()

    lateinit var quizDatabase: QuizDatabase
    lateinit var mainActivity: MainActivity
    lateinit var wordDataClass: WordDataClass
    private var questionIndex = 0
    private var currentIndex = 0
    private var score = 0
    lateinit var binding: FragmentPlayWordPuzzleBinding
    private var difficultyLevel:Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentPlayWordPuzzleBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        arguments?.let {
            difficultyLevel = it.getInt(AppConsts.quizDifficulty)
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
        quizDatabase = QuizDatabase.createDatabase(mainActivity)

        binding.btnNextWordButton.setOnClickListener {
            if (binding.etWordAnswer.text.toString().trim().isEmpty()) {
                binding.etWordAnswer.error = "Enter Your Answer"
                binding.etWordAnswer.requestFocus()
            } else {
                // Check answer of the player
                checkAnswer(binding.etWordAnswer.text.toString())


            }
        }
        getWordsFromDatabase()
    }

    private fun showWrongAnswerDialog() {
        val dialog = Dialog(mainActivity)
        dialog.setContentView(R.layout.wrong_answer)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btnNext = dialog.findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            dialog.dismiss()
            setUpNewQuestion()
        }
        dialog.show()
    }

    private fun showRightAnswerDialog() {
        val dialog = Dialog(mainActivity)
        dialog.setContentView(R.layout.right_answer)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btnNext = dialog.findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            dialog.dismiss()
            setUpNewQuestion()
        }
        dialog.show()
    }

    private fun showResult() {
        // var scoreFragment=Score()
        val bundle = Bundle()
        bundle.putInt("score", score)
        println("ScoreBoard $score")

        // mainActivity.navController.navigate(R.id.score,bundle)
        val dialog = Dialog(mainActivity)
        dialog.setContentView(R.layout.score_dialog)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val tvscore = dialog.findViewById<TextView>(R.id.tvScore)
        val dismiss = dialog.findViewById<Button>(R.id.btnDismiss)
        tvscore.text = score.toString()
        dismiss.setOnClickListener {
            dialog.dismiss()
            mainActivity.navController.popBackStack()
        }
        if (score > (wordarray.size) / 2) {
            binding.konfettiView.visibility = View.VISIBLE
            val party = Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 2000, TimeUnit.MILLISECONDS).max(700),
                position = Position.Relative(0.5, 0.3)
            )
            binding.konfettiView.start(party)
        }
        dialog.show()
    }

    private fun checkAnswer(optionIndex: String) {
        val currentQuestion = wordarray[questionIndex]
        val userInput = optionIndex.trim().lowercase()
        val correctAnswer = currentQuestion.answer.lowercase()
        if (userInput.equals(correctAnswer, ignoreCase = true)) {
            score++
            if (questionIndex < wordarray.size - 1) {
                showRightAnswerDialog()
            } else if(questionIndex == wordarray.size -1){
                showResult()
            }
        } else {
            if (questionIndex < wordarray.size - 1) {
                showWrongAnswerDialog()
            } else if(questionIndex == wordarray.size -1){
                showResult()
            }
        }
    }

    fun setUpNewQuestion(){
        // After check clear the edittext
        binding.etWordAnswer.text?.clear()

        // Increase the index of the question by 1
        questionIndex++

        if (questionIndex < wordarray.size) {
            mixWords()
        } else {
            showResult()
        }
    }


    private fun getWordsFromDatabase() {
        wordarray.clear()
        class GetAllWords : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                wordarray.addAll(quizDatabase.quizDao().getWordPuzzleList(difficultyLevel))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                if(wordarray.isEmpty()){
                    val optionDialog = AlertDialog.Builder(mainActivity).create()
                    var runner = Runnable {
                        if(mainActivity.navController.currentDestination?.id == R.id.playWordPuzzleFragment){
                            optionDialog.dismiss()
                            mainActivity.navController.popBackStack()
                        } }
                    var handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(runner, 5000)
                    optionDialog.apply {
                        setTitle(mainActivity.resources.getString(R.string.sorry))
                        setMessage(mainActivity.resources.getString(R.string.try_later))
                        setButton(mainActivity.resources.getString(R.string.ok)){_,_->
                            handler.removeCallbacks(runner)
                            mainActivity.navController.popBackStack()
                        }
                        show()
                    }

                }else
                    mixWords()
            }
        }
        GetAllWords().execute()
    }

    fun mixWords() {
        val random = Random()
        val wordDataClass: WordDataClass = wordarray[currentIndex]
        val answer = wordDataClass.answer
        binding.tvWordNumber.text =
            context?.getString(R.string.word_count, (currentIndex + 1), wordarray.size)
        val shuffledWord = answer.toCharArray().apply { shuffle() }.joinToString("")
        binding.tvWordQuestion.text = shuffledWord
        currentIndex = (currentIndex + 1) % wordarray.size

    }

    private fun exitPlayingWordPuzzle() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage("Do you want to exit from word puzzle game?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Handle the "Yes" button click, e.g., close the fragment or exit the app
            mainActivity.navController.popBackStack()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // Handle the "No" button click, e.g., dismiss the dialog
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                exitPlayingWordPuzzle()
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizordSecondFragment.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayWordPuzzleFragment().apply {
                arguments = Bundle().apply {

                }
            }

        private const val ARG_DIFFICULTY_LEVEL = "difficultyLevel"
    }
}