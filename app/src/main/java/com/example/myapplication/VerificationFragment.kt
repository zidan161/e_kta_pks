package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class VerificationFragment : Fragment(), VerificationPresenter.VerifyView {

    companion object {
        const val REQUEST_LOGIN = "login"
        const val REQUEST_REGISTER = "daftar"
        const val REQUEST_PASSWORD = "lupa_pass"
    }

    private lateinit var presenter: VerificationPresenter
    private var binding: FragmentVerificationBinding? = null
    private val bind get() = binding!!
    private var profile: ProfilePost? = null
    private var otp: Int = 0
    private var requestCode: String? = ""
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentVerificationBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        mAuth = FirebaseAuth.getInstance()
        requestCode = arguments?.getString("request")

        if(requestCode == REQUEST_REGISTER) {
            profile = arguments?.getParcelable("data")
            sendVerificationCode(profile?.mobile)
        } else {
            val phone = arguments?.getString("phone")
            sendVerificationCode(phone)
        }

        presenter = VerificationPresenter(this)

        bind.btnVerify.setOnClickListener {

            if (requestCode == REQUEST_REGISTER) {
                val otpInput = bind.edtVerify.text.toString().trim()
                if (otpInput == otp.toString()) {
                    val data: String = Gson().toJson(profile)
                    presenter.postProfile(profile?.kta!!, data)
                }
            }
        }

        return view
    }

    override fun profileSaved(requestCode: Int, id: String?) {
        if (requestCode == VerificationPresenter.REQUEST_SUCCESS){
            val pref = context!!.getSharedPreferences(AuthActivity.PREFS_NAME, Context.MODE_PRIVATE)
            pref.edit().putString(AuthActivity.ID, id).apply()
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(MainActivity.REQUEST_ID, id)
            startActivity(intent)
        }
    }

    override fun login(id: String) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.REQUEST_ID, id)
        startActivity(intent)
    }

    override fun setProfile(id: String) {
        val fragment = ChangePasswordFragment()
        val args = Bundle()
        activity!!.supportFragmentManager.beginTransaction().apply {
            replace(R.id.view_container, fragment, fragment::class.java.simpleName)
            args.putString("id", id)
            fragment.arguments = args
            addToBackStack(null)
            commit()
        }
    }
}