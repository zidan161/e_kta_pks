package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.example.myapplication.databinding.ActivityAuthBinding
import com.example.myapplication.databinding.FragmentRegisterBinding

class AuthActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "anggota_pref"
        const val ID = "kta"
    }

    private lateinit var pref: SharedPreferences
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val id: String? = pref.getString(ID, null)

        if (id != null){
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.REQUEST_ID, id)
            startActivity(intent)
            finish()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                add(R.id.view_container, LoginFragment(), LoginFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}