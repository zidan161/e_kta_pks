package com.example.myapplication

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import com.example.myapplication.databinding.FragmentRegisterBinding
import com.google.gson.Gson

class RegisterFragment : Fragment(), RegisterPresenter.RegisterView, View.OnClickListener {

    private lateinit var presenter: RegisterPresenter

    private var binding: FragmentRegisterBinding? = null
    private val bind get() = binding!!

    private var idProvince: Long? = null
    private var idCity: Long? = null
    private var idDistrict: Long? = null
    private var idVillage: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = bind.root

        bind.btnSignup.setOnClickListener(this)
        bind.btnHasAccount.setOnClickListener(this)
        presenter = RegisterPresenter(this)
        presenter.getProvince()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun setProvince(province: List<Place>) {
        val adapter = CustomArrayAdapter(context!!, province)
        (bind.tfProvince.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getCity(id)
                idProvince = id
            }
        }
    }

    override fun setCity(city: List<Place>) {
        val adapter = CustomArrayAdapter(context!!, city)
        (bind.tfCity.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getDistrict(id)
                idCity = id
            }
        }
    }

    override fun setDistrict(district: List<Place>) {
        val adapter = CustomArrayAdapter(context!!, district)
        (bind.tfDistrict.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getVillage(id)
                idDistrict = id
            }
        }
    }

    override fun setVillage(village: List<Place>?) {
        if (village != null) {
            val adapter = CustomArrayAdapter(context!!, village)
            (bind.tfVillage.editText as AutoCompleteTextView).apply {
                setAdapter(adapter)
                onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id -> idVillage = id }
            }
            bind.edtVillage.inputType = InputType.TYPE_NULL
        } else {
            bind.edtVillage.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_signup -> {
                val kta = bind.edtKta.text.toString().trim()
                val name = bind.edtNama.text.toString()
                val address = bind.edtAddress.text.toString()
                val mobile = bind.edtMobile.text.toString()
                val email = bind.edtEmail.text.toString().trim()
                val birthplace = bind.edtBirthplace.text.toString()
                val birthday = bind.edtBirthday.text.toString().trim()
                val password = bind.edtPass.text.toString().trim()

                bind.edtKta.isError(kta)
                bind.edtNama.isError(name)
                bind.edtEmail.isError(email)
                bind.edtAddress.isError(address)
                bind.edtMobile.isError(mobile)
                bind.edtBirthplace.isError(birthplace)
                bind.edtBirthday.isError(birthday)
                bind.edtPass.isError(password)

                val data = ProfilePost(
                        kta, name, address, "",
                        mobile, mobile, email,
                        idProvince.toString(), idCity.toString(), idDistrict.toString(), idVillage.toString(), "True", birthplace, birthday, password)
                val gson = Gson()
                val rawFile = gson.toJson(data)

                presenter.addAnggota(kta, rawFile)
            }
            R.id.btn_has_account -> {
                activity!!.supportFragmentManager.beginTransaction().apply {
                    replace(R.id.view_container, LoginFragment(), LoginFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    override fun profileSaved(responseCode: Int, id: String?) {

    }
}