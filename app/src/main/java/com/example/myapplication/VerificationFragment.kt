package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentVerificationBinding

class VerificationFragment : Fragment(){

    private var binding: FragmentVerificationBinding? = null
    private val bind get() = binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentVerificationBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        bind.btnVerify.setOnClickListener {
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}