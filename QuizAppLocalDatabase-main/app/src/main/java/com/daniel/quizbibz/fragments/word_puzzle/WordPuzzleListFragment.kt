package com.daniel.quizbibz.fragments.word_puzzle

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.adapters.WordRecyclerViewAdapter
import com.daniel.quizbibz.database.QuizDatabase
import com.daniel.quizbibz.databinding.FragmentWordPuzzleListBinding
import com.daniel.quizbibz.models.WordDataClass
import com.daniel.quizbibz.utils.WordInterface


class WordPuzzleListFragment : Fragment(), WordInterface {
    lateinit var binding: FragmentWordPuzzleListBinding
    lateinit var mainActivity: MainActivity
    lateinit var quizDatabase: QuizDatabase
    lateinit var wordRecyclerViewAdapter: WordRecyclerViewAdapter
    var wordarray = arrayListOf<WordDataClass>()
    private var difficultyLevel: Int? = 1

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
        binding = FragmentWordPuzzleListBinding.inflate(layoutInflater)

        difficultyLevel = arguments?.getInt("difficultyLevel", 1) ?: 0

        quizDatabase = QuizDatabase.createDatabase(mainActivity)

        // Set the data on the recycler view
        wordRecyclerViewAdapter = WordRecyclerViewAdapter(mainActivity, wordarray, this)
        binding.wordListview.layoutManager = LinearLayoutManager(mainActivity)
        binding.wordListview.adapter = wordRecyclerViewAdapter
        binding.wordListview.animation = AnimationUtils.loadAnimation(context,R.anim.recycler_view_adapter_animation)

        binding.btnWordfab.setOnClickListener {
            openAddUpdateWordDialog()
        }

        getWordData()

        return binding.root
    }

    fun getWordData() {
        wordarray.clear()
        class GetWord : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                wordarray.addAll(quizDatabase.quizDao().getWordPuzzleList(difficultyLevel))
                return null
            }

            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                wordRecyclerViewAdapter.notifyDataSetChanged()
            }
        }
        GetWord().execute()
    }

    private fun openAddUpdateWordDialog(
        position: Int = -1,
    ) {
        val dialog = Dialog(mainActivity)
        dialog.setContentView(R.layout.add_word)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(true)

        val title = dialog.findViewById<TextView>(R.id.tvTitle)
        val etQuestion = dialog.findViewById<EditText>(R.id.etQuestion)
        val btnAddWord = dialog.findViewById<Button>(R.id.btnAddWord)


        if (position > -1) {
            title.text = context?.getString(R.string.update_word)
            etQuestion.setText(wordarray[position].answer)
            btnAddWord.text = context?.getString(R.string.update)
        }

        btnAddWord.setOnClickListener {
            if (etQuestion.text.toString().isEmpty()) {
                etQuestion.error = "Enter Question"
            } else {
                if (position == -1) {
                    class InsertQuestion : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void?): Void? {
                            val wordDataClass =
                                WordDataClass(
                                    answer = etQuestion.text.toString(),
                                    type = difficultyLevel
                                )
                            quizDatabase.quizDao().insertIntoWord(wordDataClass)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            dialog.dismiss()
                            getWordData()
                        }
                    }
                    InsertQuestion().execute()
                } else {
                    class UpdateWord : AsyncTask<Void, Void, Void>() {
                        override fun doInBackground(vararg params: Void?): Void? {
                            val wordDataClass =
                                WordDataClass(
                                    q_num = wordarray[position].q_num,
                                    answer = etQuestion.text.toString(),
                                    type = difficultyLevel
                                )
                            quizDatabase.quizDao().updateWordQuestion(wordDataClass)
                            return null
                        }

                        override fun onPostExecute(result: Void?) {
                            super.onPostExecute(result)
                            dialog.dismiss()
                            getWordData()
                        }
                    }
                    UpdateWord().execute()
                }
            }
        }
        dialog.show()
    }

    override fun updateWordItem(position: Int) {
        openAddUpdateWordDialog(
            position = position,
        )
    }

    override fun deleteWordItem(wordDataClass: WordDataClass) {
        MaterialAlertDialogBuilder(mainActivity).apply {
            setMessage("Do you really want to delete this word puzzle?")
            setPositiveButton("Yes"){_,_->
                class DeleteWordQuestion : AsyncTask<Void, Void, Void>() {
                    override fun doInBackground(vararg params: Void?): Void? {
                        quizDatabase.quizDao().deleteWordQuestion(wordDataClass)
                        return null
                    }

                    override fun onPostExecute(result: Void?) {
                        super.onPostExecute(result)
                        getWordData()
                    }
                }
                DeleteWordQuestion().execute()
            }
            setNegativeButton("No"){_,_->}
            show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WordPuzzleListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int) =
            WordPuzzleListFragment().apply {
                arguments = Bundle().apply {
                    putInt("difficultyLevel", param1)
                }
            }
    }
}