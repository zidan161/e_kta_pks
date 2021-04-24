package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import com.example.myapplication.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(), View.OnClickListener, LoginPresenter.LoginView {

    private lateinit var presenter: LoginPresenter
    private var binding: FragmentLoginBinding? = null
    private val bind get() = binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = bind.root

        presenter = LoginPresenter(requireContext(), this)

        bind.btnLogin.setOnClickListener (this)
        bind.tvPass.setOnClickListener (this)
        bind.tvBtn.setOnClickListener (this)

        return view
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_login -> {
                val mobile = bind.edtPhone.text.toString().trim()
                val password = bind.edtPass.text.toString().trim()

                bind.edtPhone.isError(mobile)
                bind.edtPass.isError(password)
                if (!isNetworkAvailable(requireContext())){
                    Snackbar.make(requireView(), "Tidak ada sambungan internet", Snackbar.LENGTH_SHORT).show()
                    return
                }
                presenter.getAnggota(mobile, password)
            }R.id.tv_btn -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    replace(R.id.view_container, RegisterFragment(), RegisterFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            } R.id.tv_pass -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    replace(R.id.view_container, ChangePasswordFragment(), ChangePasswordFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun isRegistered(requestCode: Int, data: Profile?) {
        if (requestCode == LoginPresenter.REQUEST_AVAILABLE){
            val pref = requireContext().getSharedPreferences(AuthActivity.PREFS_NAME, Context.MODE_PRIVATE)
            pref.edit().putString(AuthActivity.ID, data!!.kta).apply()
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(MainActivity.REQUEST_ID, data.kta)
            startActivity(intent)
            requireActivity().finish()
        } else {
            Toast.makeText(context, "No. hp atau password salah!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().finish()
        binding = null
    }
}