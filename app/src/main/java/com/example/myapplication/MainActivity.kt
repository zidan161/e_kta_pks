package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_ID = "kta"
    }

    private lateinit var binding: ActivityMainBinding
    private val fragment1 = MainFragment()
    private val fragment2 = ProfileFragment()

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
                add(R.id.nav_host_fragment, fragment2, ProfileFragment::class.java.simpleName).hide(fragment2)
                add(R.id.nav_host_fragment, fragment1, MainFragment::class.java.simpleName)
            }.commit()

            binding.navView.setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.navigation_main -> {
                        fm.beginTransaction().hide(fragment2).show(fragment1).commit()
                    }
                    R.id.navigation_profile -> {
                        fm.beginTransaction().hide(fragment1).show(fragment2).commit()
                    }
                }
                true
            }
        }
    }
}