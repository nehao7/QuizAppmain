package com.daniel.quizbibz.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.databinding.WordListItemAdapterBinding
import com.daniel.quizbibz.models.WordDataClass
import com.daniel.quizbibz.utils.WordInterface
import java.util.Locale


class WordRecyclerViewAdapter(
    val context: Context,
    private val wordPuzzleList: List<WordDataClass>, var wordInterface: WordInterface
) : RecyclerView.Adapter<WordRecyclerViewAdapter.ViewHolder>() {
    lateinit var mainActivity: MainActivity

    class ViewHolder(var binding: WordListItemAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            wordDataClass: WordDataClass,
            position: Int,
            wordInterface: WordInterface,
            context: Context
        ) {
//            binding.tvWordNumber.text = context.getString(R.string.word_number, wordDataClass.q_num)
            binding.tvWordName.text = wordDataClass.answer.capitalize(Locale.ROOT)

            /*when (wordDataClass.type) {
                1 -> {
                    binding.tvWordType.setBackgroundResource(R.drawable.text_view_shape_easy)
                    binding.tvWordType.text = context.getString(R.string.Easy)
                }

                2 -> {
                    binding.tvWordType.setBackgroundResource(R.drawable.text_view_shape_medium)
                    binding.tvWordType.text = context.getString(R.string.MEDIUM)
                }

                else -> {
                    binding.tvWordType.setBackgroundResource(R.drawable.text_view_shape_hard)
                    binding.tvWordType.text = context.getString(R.string.HARD)
                }
            }*/

            binding.btnWordDelete.setOnClickListener {
                wordInterface.deleteWordItem(wordDataClass)
            }
            binding.btnWordUpdate.setOnClickListener {
                wordInterface.updateWordItem(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            WordListItemAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = wordPuzzleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(wordPuzzleList[position], position, wordInterface, context)
    }
}
