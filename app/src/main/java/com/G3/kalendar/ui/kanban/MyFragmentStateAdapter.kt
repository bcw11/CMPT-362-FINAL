package com.G3.kalendar.ui.kanban

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class MyFragmentStateAdapter(fragment: Fragment, var list: ArrayList<Fragment>)
    : FragmentStateAdapter(fragment){

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}