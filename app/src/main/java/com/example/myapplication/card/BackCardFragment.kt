package com.example.myapplication.card

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import com.example.myapplication.R

class BackCardFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_back_card, container, false)
    }
}