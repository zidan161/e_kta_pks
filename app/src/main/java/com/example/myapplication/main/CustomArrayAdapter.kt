package com.example.myapplication.main

import android.content.Context
import android.widget.ArrayAdapter
import com.example.myapplication.models.Place
import com.example.myapplication.R

class CustomArrayAdapter(context: Context, places: List<Place>) : ArrayAdapter<Place>(context, R.layout.list_item, places){

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!.toLong()
    }
}