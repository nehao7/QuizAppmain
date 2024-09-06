package com.daniel.quizbibz.fragments.quiz

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.database.QuizDatabase
import com.daniel.quizbibz.databinding.FragmentPlayQuizBinding
import com.daniel.quizbibz.models.QuizDataClass
import com.daniel.quizbibz.utils.AppConsts
import com.daniel.quizbibz.utils.HelperFunctions
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


class PlayQuizFragment : Fragment() {

    lateinit var quizDatabase: QuizDatabase
    lateinit var mainActivity: MainActivity
    private var questionIndex = 0
    private var score = 0
    private var selectedOption: Int? = 0
    var quizarray = arrayListOf<QuizDataClass>()
    lateinit var binding: FragmentPlayQuizBinding
    private var quizTimer: CountDownTimer? = null
    lateinit var helperFunctions: HelperFunctions
    private var difficultyLevel: Int = 1
    private var TAG = "PlayQuizFragment"
    private var attemptedQuestions = 0
    private var missedQuestions = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        quizDatabase = QuizDatabase.createDatabase(mainActivity)
        helperFunctions = HelperFunctions()
        arguments?.let {
            difficultyLevel = it.getInt(AppConsts.quizDifficulty, 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPlayQuizBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getQuiz()

        binding.tvQuizOptionA.setOnClickListener { view ->
            selectOption(1)
        }
        binding.tvQuizOptionB.setOnClickListener { view ->
            selectOption(2)
        }
        binding.tvQuizOptionC.setOnClickListener { view ->
            selectOption(3)
        }
        binding.tvQuizOptionD.setOnClickListener { view ->
            selectOption(4)
        }
        binding.btnQuizNext.setOnClickListener {
            if (selectedOption != null) {
                val currentQuestion = quizarray[questionIndex]
                if (selectedOption == currentQuestion.correct_option) {
                    score++
                    if (questionIndex < quizarray.size - 1) {
                        showRightAnswerDialog()
                    }
                } else {
                    if (questionIndex < quizarray.size - 1) {
                        showWrongAnswerDialog()
                    }
                }
                questionIndex++
                selectedOption = null
                if (questionIndex < quizarray.size) {
                    updateQuestion()
                } else {
                    showResult()
                }
            } else {
                Toast.makeText(mainActivity, "Choose an option", Toast.LENGTH_SHORT).show()
            }
        }
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
            startTimerForCurrentQuestion()
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
            startTimerForCurrentQuestion()
        }
        dialog.show()
    }

    private fun showResult() {
        val bundle = Bundle()
        bundle.putInt("score", score)
        println("ScoreBoard $score")

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
        if (score > (quizarray.size) / 2) {
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

    private fun getQuiz() {
        class GetAllQuestions : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                quizarray.addAll(quizDatabase.quizDao().getQuizQuestionList(difficultyLevel))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                if (quizarray.isNotEmpty()) {
                    startTimerForCurrentQuestion()
                    updateQuestion()
                }else {
                    val optionDialog = AlertDialog.Builder(mainActivity).create()
                    var runner = Runnable {
                        if(mainActivity.navController.currentDestination?.id == R.id.playQuizFragment){
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

                }
            }
        }
        GetAllQuestions().execute()
    }

    fun updateQuestion() {
        selectedOption = null
        binding.btnQuizNext.visibility = View.GONE
        val currentQuestion = quizarray[questionIndex]
        binding.currentQuizData = currentQuestion

        //binding.tvAttemptQuestion.text = (questionIndex + 1).toString()
        binding.tvAttemptQuestion.text  = attemptedQuestions.toString()
        binding.tvTotalQuestion.text = quizarray.size.toString()

        resetViews()


    }

    private fun resetViews() {
        binding.tvQuizOptionA.setBackgroundResource(R.drawable.text_view_option_outline_shape)
        binding.tvQuizOptionA.setPadding(50, 50, 50, 50)

        binding.tvQuizOptionB.setBackgroundResource(R.drawable.text_view_option_outline_shape)
        binding.tvQuizOptionB.setPadding(50, 50, 50, 50)

        binding.tvQuizOptionC.setBackgroundResource(R.drawable.text_view_option_outline_shape)
        binding.tvQuizOptionC.setPadding(50, 50, 50, 50)

        binding.tvQuizOptionD.setBackgroundResource(R.drawable.text_view_option_outline_shape)
        binding.tvQuizOptionD.setPadding(50, 50, 50, 50)
    }

    private fun startTimerForCurrentQuestion() {
        val currentQuestionTime = (quizarray[questionIndex].questionTime?:"0").toInt()
        val questionTimeString = "00:${currentQuestionTime.toString().padStart(2, '0')}"

        // Get the time for the current question in milliseconds
        val totalTimeInMillis = helperFunctions.convertTimeToMilliseconds(questionTimeString)

        // Set total time text
        val totalMinutes = TimeUnit.MILLISECONDS.toMinutes(totalTimeInMillis)
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(totalTimeInMillis) % 60
        val totalTimeString = String.format("%02d:%02d", totalMinutes, totalSeconds)
        //binding.tvTotalTime.text = totalTimeString

        // Start the countdown timer
        quizTimer?.cancel() // Cancel any existing timer
        quizTimer = object : CountDownTimer(totalTimeInMillis, 1000) { // Update every second
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val totalSeconds = totalTimeInMillis / 1000

                val remainingMinutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val remainingSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                val remainingTimeString =
                    String.format("%02d:%02d", remainingMinutes, remainingSeconds)

                // Set remaining time text



                //binding.tvQuizTimer.text = "$secondsRemaining / $totalSeconds" // Update the timer text
            }

            override fun onFinish() {

                Log.d(TAG, "onFinish: SelectedOption $selectedOption")
                if (selectedOption == null) {
                    showMissedQuestionDialog()
                } else {
                    if (questionIndex < quizarray.size - 1) {

                        questionIndex++
                        selectedOption = null
                        updateQuestion()
                    } else {
                        showResult()
                    }
                }
            }
        }.start()
    }

    private fun selectOption(optionIndex: Int) {
        resetViews()
        selectedOption = optionIndex

        binding.btnQuizNext.visibility = View.VISIBLE
        when (optionIndex) {
            1 -> {
                binding.tvQuizOptionA.setBackgroundResource(R.drawable.text_view_option_shape)
                binding.tvQuizOptionA.setPadding(50, 50, 50, 50)
            }

            2 -> {
                binding.tvQuizOptionB.setBackgroundResource(R.drawable.text_view_option_shape)
                binding.tvQuizOptionB.setPadding(50, 50, 50, 50)
            }

            3 -> {
                binding.tvQuizOptionC.setBackgroundResource(R.drawable.text_view_option_shape)
                binding.tvQuizOptionC.setPadding(50, 50, 50, 50)
            }

            4 -> {
                binding.tvQuizOptionD.setBackgroundResource(R.drawable.text_view_option_shape)
                binding.tvQuizOptionD.setPadding(50, 50, 50, 50)
            }
        }

        attemptedQuestions++
    }

    private fun showMissedQuestionDialog() {
        val dialog = Dialog(mainActivity)
        dialog.setContentView(R.layout.missed_question)
        dialog.setCancelable(false)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        val btnNext = dialog.findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            if (questionIndex < quizarray.size - 1) {
                questionIndex++
                selectedOption = null
                updateQuestion()
                dialog.dismiss()
            }else{
                showResult()
                dialog.dismiss()
            }
            startTimerForCurrentQuestion()
        }
        dialog.show()
    }

    private fun exitPlayingQuiz() {
        quizTimer?.cancel()
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("Alert")
        alertDialogBuilder.setMessage("Do you want to exit from quiz?")

        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // Handle the "Yes" button click, e.g., close the fragment or exit the app
            quizTimer?.cancel()
            mainActivity.navController.popBackStack()
        }

        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // Handle the "No" button click, e.g., dismiss the dialog
            dialog.dismiss()
            quizTimer?.start()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        quizTimer?.cancel()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(this,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                exitPlayingQuiz()
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
         * @return A new instance of fragment PlayQuizFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlayQuizFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}