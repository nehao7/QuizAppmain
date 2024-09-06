package com.daniel.quizbibz.fragments.quiz

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.adapters.QuizRecyclerViewAdapter
import com.daniel.quizbibz.database.QuizDatabase
import com.daniel.quizbibz.databinding.AddQuestionBinding
import com.daniel.quizbibz.databinding.FragmentQuizListBinding
import com.daniel.quizbibz.models.QuizDataClass
import com.daniel.quizbibz.utils.QuizInterface


class QuizListFragment : Fragment(), QuizInterface {
    lateinit var binding: FragmentQuizListBinding
    lateinit var mainActivity: MainActivity
    lateinit var quizRecyclerViewAdapter: QuizRecyclerViewAdapter
    lateinit var quizDatabase: QuizDatabase
    var quizArray = arrayListOf<QuizDataClass>()
    private var difficultyLevel: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentQuizListBinding.inflate(layoutInflater)

        difficultyLevel = arguments?.getInt("difficultyLevel", 1) ?: 1

        quizDatabase = QuizDatabase.createDatabase(mainActivity)

        // Set the adapter on the recycler
        quizRecyclerViewAdapter = QuizRecyclerViewAdapter(mainActivity, quizArray, this)
        binding.quizListview.layoutManager = LinearLayoutManager(mainActivity)
        binding.quizListview.adapter = quizRecyclerViewAdapter
        binding.quizListview.animation =
            AnimationUtils.loadAnimation(context, R.anim.recycler_view_adapter_animation)

        binding.btnQuizfab.setOnClickListener {
            openAddUpdateQuizDialog()
        }

        getQuizData()

        return binding.root
    }

    fun getQuizData() {
        quizArray.clear()
        class GetAllQuiz : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                quizArray.addAll(quizDatabase.quizDao().getQuizQuestionList(difficultyLevel))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                quizRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
        GetAllQuiz().execute()
    }

    private fun openAddUpdateQuizDialog(
        position: Int = -1,
    ) {
        val dialog = Dialog(mainActivity)
        val dialogBinding = AddQuestionBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        if (position > -1) {
            dialogBinding.tvQuizTitle.text = context?.getString(R.string.update_question)
            dialogBinding.etQuestion.setText(quizArray[position].question)
            dialogBinding.etOptionA.setText(quizArray[position].option_a)
            dialogBinding.etOptionB.setText(quizArray[position].option_b)
            dialogBinding.etOptionC.setText(quizArray[position].option_c)
            dialogBinding.etOptionD.setText(quizArray[position].option_d)
            dialogBinding.etQuestionOptionCorrect.setText(quizArray[position].correct_option.toString())
            dialogBinding.etQuestionTimer.setText(quizArray[position].questionTime)
            dialogBinding.btnAddQuestion.text = context?.getString(R.string.update)
        }

        dialogBinding.btnAddQuestion.setOnClickListener {
            if (dialogBinding.etQuestion.text.toString().isEmpty()) {
                dialogBinding.etQuestion.error = "Enter question first"
            } else if (dialogBinding.etOptionA.text.toString().isEmpty()) {
                dialogBinding.etOptionA.error = "Enter option A first"
            } else if (dialogBinding.etOptionB.text.toString().isEmpty()) {
                dialogBinding.etOptionB.error = "Enter option B first"
            } else if (dialogBinding.etOptionC.text.toString().isEmpty()) {
                dialogBinding.etOptionC.error = "Enter option C first"
            } else if (dialogBinding.etOptionD.text.toString().isEmpty()) {
                dialogBinding.etOptionD.error = "Enter option D first"
            } else if (dialogBinding.etQuestionOptionCorrect.text.toString().isEmpty()) {
                dialogBinding.etQuestionOptionCorrect.error = "Enter correct option first"
            }else if (dialogBinding.etQuestionTimer.text.toString().isEmpty()) {
                dialogBinding.etQuestionTimer.error = "Select time for this question first"
            } else {
                val quizDataClass = QuizDataClass(
                    question = dialogBinding.etQuestion.text.toString(),
                    option_a = dialogBinding.etOptionA.text.toString(),
                    option_b = dialogBinding.etOptionB.text.toString(),
                    option_c = dialogBinding.etOptionC.text.toString(),
                    option_d = dialogBinding.etOptionD.text.toString(),
                    correct_option = dialogBinding.etQuestionOptionCorrect.text.toString().toInt(),
                    questionType = difficultyLevel,
                    questionTime = dialogBinding.etQuestionTimer.text.toString()
                )
                if (position == -1) {
                    class InsertQuestion : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void?): Void? {
                            //when saving the text in database get the text from edittext don't save it directly
                            quizDatabase.quizDao().insertIntoQuiz(quizDataClass)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            dialog.dismiss()
                            getQuizData()
                        }
                    }
                    InsertQuestion().execute()
                } else {
                    class UpdateQuestion : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void?): Void? {
                            quizDataClass.q_number = quizArray[position].q_number
                            quizDatabase.quizDao().updateQuestion(quizDataClass)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            dialog.dismiss()
                            getQuizData()
                        }
                    }
                    UpdateQuestion().execute()
                }
            }
        }
        dialog.show()
    }

    override fun deleteQuizItem(quizDataClass: QuizDataClass) {
        MaterialAlertDialogBuilder(mainActivity).apply {
             setMessage("Do you really want to delete this question data?")
             setPositiveButton("Yes"){_,_->
                class DeleteQuestion : AsyncTask<Void, Void, Void>() {
                    override fun doInBackground(vararg params: Void?): Void? {
                        quizDatabase.quizDao().deleteQuestion(quizDataClass)
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getQuizData()
                    }
                }
                DeleteQuestion().execute()
             }
             setNegativeButton("No"){_,_->}
             show()
         }
    }

    override fun updateQuizItem(
        position: Int,
    ) {
        openAddUpdateQuizDialog(
            position = position
        )

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(quizType: Int) =
            QuizListFragment().apply {
                arguments = Bundle().apply {
                    putInt("difficultyLevel", quizType)
                }
            }
    }
}