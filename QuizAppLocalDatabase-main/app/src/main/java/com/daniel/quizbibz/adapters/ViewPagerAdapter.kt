package com.daniel.quizbibz.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragment: FragmentActivity, var mFragmentList: ArrayList<Fragment>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = mFragmentList.size

    override fun createFragment(position: Int): Fragment {
        return mFragmentList[position]
    }

}
