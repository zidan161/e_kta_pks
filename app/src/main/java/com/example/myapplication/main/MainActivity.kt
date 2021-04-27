package com.example.myapplication.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.isNetworkAvailable

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_ID = "kta"
    }

    private lateinit var binding: ActivityMainBinding
    private val fragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternet()

        binding.btnTryAgain.setOnClickListener {
            checkInternet()
        }
    }

    private fun checkInternet() {
        if (!isNetworkAvailable(this)) {
            binding.tvNoInternet.visibility = View.VISIBLE
            binding.btnTryAgain.visibility = View.VISIBLE
        } else {
            binding.tvNoInternet.visibility = View.GONE
            binding.btnTryAgain.visibility = View.GONE

            val fm = supportFragmentManager

            fm.beginTransaction().apply {
                add(R.id.nav_host_fragment, fragment, ProfileFragment::class.java.simpleName)
            }.commit()
        }
    }
}