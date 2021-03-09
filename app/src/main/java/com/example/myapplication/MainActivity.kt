package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val REQUEST_ID = "kta"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btmNav.setOnNavigationItemSelectedListener { items ->
            when(items.itemId){
                R.id.btn_feed -> {
                    val fragment = MainFragment()
                    addfragment(fragment, savedInstanceState)
                } R.id.btn_profile -> {
                val fragment = ProfileFragment()
                addfragment(fragment, savedInstanceState)
                }
            }
            true
        }
        binding.btmNav.selectedItemId = R.id.btn_feed
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

    }

    private fun addfragment(fragment: Fragment, savedInstanceState: Bundle?){
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.view_container, fragment, fragment.javaClass.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    override fun onBackPressed() {
        finish()
        AuthActivity().finish()
    }
}