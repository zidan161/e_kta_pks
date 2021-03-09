package com.example.myapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

@Suppress("DEPRECATION")
class CardAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {

        var fm = Fragment()

        when(position){
            0 -> {
                fm = FrontCardFragment()
            }1 -> {
                fm = BackCardFragment()
            }
        }

        return fm
    }
}