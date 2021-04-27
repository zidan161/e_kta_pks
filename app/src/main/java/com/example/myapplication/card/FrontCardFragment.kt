package com.example.myapplication.card

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.main.MainActivity
import com.example.myapplication.models.Profile
import com.example.myapplication.databinding.FragmentFrontCardBinding
import com.squareup.picasso.Picasso

class FrontCardFragment : Fragment(), CardPresenter.CardView {

    companion object {
        private var url = "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data="
    }
    private lateinit var presenter: CardPresenter

    private var binding: FragmentFrontCardBinding? = null
    private val bind get() = binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentFrontCardBinding.inflate(inflater, container, false)
        val view = bind.root

        val id = requireActivity().intent.getStringExtra(MainActivity.REQUEST_ID)

        presenter = CardPresenter(this)
        presenter.getAnggota(id)

        Picasso.get().load(url + id).into(bind.imgQr)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun setProfile(profile: Profile) {
        bind.tvKtaVal.text = profile.kta
        bind.tvNameVal.text = profile.name
        bind.tvAddressVal.text = "${profile.street}, ${profile.village[0].name}, ${profile.district[0].name}, ${profile.city[0].name}, ${profile.province[0].name}"
        bind.tvMobileVal.text = profile.mobile
        bind.tvBirthdayVal.text = "${profile.birthPlace}, ${profile.birthDay}"
        bind.tvPrintOfVal.text = profile.activationDate
    }
}