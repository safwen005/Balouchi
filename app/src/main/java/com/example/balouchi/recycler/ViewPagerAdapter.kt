package com.example.balouchi.recycler

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(s:FragmentManager,var array:Array<Fragment>) : FragmentStatePagerAdapter(s,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
       return array[position]
    }

    override fun getCount(): Int {
      return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab $position"
    }
}