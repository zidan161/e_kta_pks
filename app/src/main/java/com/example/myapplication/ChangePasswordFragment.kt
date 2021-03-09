package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment(), ChangePasswordPresenter.PasswordView {

    private var binding: FragmentChangePasswordBinding? = null
    private val bind get() = binding!!
    private lateinit var presenter: ChangePasswordPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        presenter = ChangePasswordPresenter(this)
        val id = arguments!!.getString("id")

        bind.btnNext.setOnClickListener {
            val pass = bind.edtChangePass.text.toString().trim()
            if (pass.isEmpty()){
                bind.edtChangePass.error = "Field ini harus diisi!"
                return@setOnClickListener
            }
            presenter.changePassword(id, pass)
        }
        return view
    }

    override fun passwordSaved() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }
}