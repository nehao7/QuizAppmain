package com.daniel.quizbibz.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.daniel.quizbibz.R
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.databinding.QuizListItemAdapterBinding
import com.daniel.quizbibz.models.QuizDataClass
import com.daniel.quizbibz.utils.QuizInterface


class QuizRecyclerViewAdapter(
    var context: Context,
    private var quizList: ArrayList<QuizDataClass>,
    private var quizInterface: QuizInterface
) :
    RecyclerView.Adapter<QuizRecyclerViewAdapter.ViewHolder>() {
    lateinit var mainActivity: MainActivity

    class ViewHolder(var binding: QuizListItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            quizDataClass: QuizDataClass,
            position: Int,
            quizInterface: QuizInterface,
            context: Context
        ) {
            binding.quizData = quizDataClass
            binding.position = position

            binding.qNumber.text =
                context.getString(R.string.question_number, quizDataClass.q_number)


            binding.tvCOption.text = when (quizDataClass.correct_option) {
                1 -> {
                    context.getString(R.string.correct_answer, "A")
                }

                2 -> {
                     context.getString(R.string.correct_answer, "B")
                }

                3 -> {
                     context.getString(R.string.correct_answer, "C")
                }

                4 -> {
                     context.getString(R.string.correct_answer, "D")
                }

                else -> { "" }
            }

            when (quizDataClass.questionType) {
                1 -> {
                    binding.tvLevelType.setBackgroundResource(R.drawable.text_view_shape_easy)
                    binding.tvLevelType.text = context.getString(R.string.Easy)
                }

                2 -> {
                    binding.tvLevelType.setBackgroundResource(R.drawable.text_view_shape_medium)
                    binding.tvLevelType.text = context.getString(R.string.MEDIUM)
                }

                else -> {
                    binding.tvLevelType.setBackgroundResource(R.drawable.text_view_shape_hard)
                    binding.tvLevelType.text = context.getString(R.string.HARD)
                }
            }

            binding.btnUpdate.setOnClickListener {
                quizInterface.updateQuizItem(position)
            }
            binding.btnDelete.setOnClickListener {
                quizInterface.deleteQuizItem(quizDataClass)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            QuizListItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return quizList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quizList[position], position, quizInterface, context)
    }
}
