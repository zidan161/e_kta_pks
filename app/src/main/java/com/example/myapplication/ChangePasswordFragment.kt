package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import com.example.myapplication.databinding.FragmentChangePasswordBinding
import com.google.gson.Gson

class ChangePasswordFragment : Fragment(), ChangePasswordPresenter.PasswordView {

    private var binding: FragmentChangePasswordBinding? = null
    private val bind get() = binding!!
    private var id: String? = null
    private lateinit var presenter: ChangePasswordPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        presenter = ChangePasswordPresenter(requireContext(), this)

        bind.btnCheckPhone.setOnClickListener {
            val phone = bind.edtPhone.text.toString().trim()
            presenter.checkAnggota(phone)
        }

        bind.btnNext.setOnClickListener {
            val pass = bind.edtChangePass.text.toString().trim()
            val gson = Gson()
            if (pass.isEmpty()){
                bind.edtChangePass.error = "Field ini harus diisi!"
                return@setOnClickListener
            }

            val password = mapOf("passwd_kta" to pass)
            presenter.changePassword(id, gson.toJson(password))
        }
        return view
    }

    override fun isRegistered(id: String?) {
        bind.edtChangePass.isEnabled = true
        bind.tfPass.isEnabled = true
        this.id = id
    }

    override fun passwordSaved() {
        Toast.makeText(context, "Password berhasil diubah", Toast.LENGTH_SHORT).show()
        val intent = Intent(context, AuthActivity::class.java)
        startActivity(intent)
    }
}