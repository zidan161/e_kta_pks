package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomArrayAdapter(context: Context, places: List<Place>) : ArrayAdapter<Place>(context, R.layout.list_item, places){

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id!!.toLong()
    }
}