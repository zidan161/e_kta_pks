package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(), ProfilePresenter.ProfileView {

    private var binding: FragmentProfileBinding? = null
    private val bind get() = binding!!
    private lateinit var presenter: ProfilePresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        val view = bind.root

        val id: String = activity?.intent?.getStringExtra(MainActivity.REQUEST_ID)!!

        presenter = ProfilePresenter(this)
        presenter.getAnggota(id)

        bind.btnToCard.setOnClickListener {
            val intent = Intent(context, CardActivity::class.java)
            intent.putExtra(MainActivity.REQUEST_ID, id)
            startActivity(intent)
        }

        bind.btnLogout.setOnClickListener {
            val pref = context!!.getSharedPreferences(AuthActivity.PREFS_NAME, Context.MODE_PRIVATE)
            pref.edit().putString(AuthActivity.ID, null).apply()
            val intent = Intent(context, AuthActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun setProfile(profile: Profile?) {
        bind.tvKta.text = "No. Kta: $id"
        bind.tvName.text = profile?.name
        bind.tvTelpVal.text = profile?.mobile
        bind.tvEmailVal.text = profile?.email
        bind.tvAddress2.text = "${profile!!.street}, ${profile.village[0].name}, ${profile.district[0].name}, ${profile.city[0].name}, ${profile.province[0].name}"
    }
}