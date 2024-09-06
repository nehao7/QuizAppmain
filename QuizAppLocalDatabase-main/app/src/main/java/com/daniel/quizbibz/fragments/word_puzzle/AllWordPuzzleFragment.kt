package com.daniel.quizbibz.fragments.word_puzzle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.daniel.quizbibz.activities.MainActivity
import com.daniel.quizbibz.adapters.ViewPagerAdapter
import com.daniel.quizbibz.databinding.FragmentAllWordPuzzleBinding

class AllWordPuzzleFragment : Fragment() {
    lateinit var binding: FragmentAllWordPuzzleBinding
    lateinit var mainActivity: MainActivity
    lateinit var viewPagerAdapter: ViewPagerAdapter
    var titles = arrayListOf("Easy", "Medium", "Hard")
    var fragments = arrayListOf<Fragment>(
        WordPuzzleListFragment.newInstance(1),
        WordPuzzleListFragment.newInstance(2),
        WordPuzzleListFragment.newInstance(3)
    )
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
        binding = FragmentAllWordPuzzleBinding.inflate(layoutInflater)

        mainActivity.supportActionBar.apply { binding.toolbar }

        viewPagerAdapter = ViewPagerAdapter(mainActivity, fragments)
        binding.viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        return binding.root
    }

}