package com.example.balouchi.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.balouchi.ui.profile_items.profile_items

class profile_viewpager(s: FragmentManager, var array: Array<profile_items?>) : FragmentStatePagerAdapter(s,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return array[position]!!
    }

    override fun getCount(): Int {
        return array.size
    }


}