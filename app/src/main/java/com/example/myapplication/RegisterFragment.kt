package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
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
        val adapter = CustomArrayAdapter(requireContext(), province)
        (bind.tfProvince.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getCity(id)
                bind.edtCity.text = null
                bind.edtDistrict.text = null
                bind.edtVillage.text = null
                if (id == 49.toLong()){
                    bind.tfVillage.visibility = View.VISIBLE
                    idProvince = id
                } else {
                    bind.tfVillage.visibility = View.GONE
                    idProvince = null
                }
            }
        }
    }

    override fun setCity(city: List<Place>) {
        val adapter = CustomArrayAdapter(requireContext(), city)
        (bind.tfCity.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getDistrict(id)
                idCity = id
                bind.edtDistrict.text = null
                bind.edtVillage.text = null
            }
        }
    }

    override fun setDistrict(district: List<Place>) {
        val adapter = CustomArrayAdapter(requireContext(), district)
        (bind.tfDistrict.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getVillage(id)
                idDistrict = id
                bind.edtVillage.text = null
            }
        }
    }

    override fun setVillage(village: List<Place>?) {
        if (village != null) {
            val adapter = CustomArrayAdapter(requireContext(), village)
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

        when(v.id) {
            R.id.btn_signup -> {
                postProfile()
            }
            R.id.btn_has_account -> {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    replace(R.id.view_container, LoginFragment(), LoginFragment::class.java.simpleName)
                    addToBackStack(null)
                    commit()
                }
            }
        }
    }

    private fun postProfile(){

        val kta = bind.edtKta.text.toString().trim()
        val name = bind.edtNama.text.toString()
        val address = bind.edtAddress.text.toString()
        val mobile = bind.edtMobile.text.toString()
        val email = bind.edtEmail.text.toString().trim()
        val birthplace = bind.edtBirthplace.text.toString()
        val birthday = bind.edtBirthday.text.toString().trim()
        val birthmonth = bind.edtBirthmonth.text.toString().trim()
        val birthyear = bind.edtBirthyear.text.toString().trim()
        val password = bind.edtPass.text.toString().trim()
        val village = if (idVillage == null){ "" } else { idVillage.toString() }

        bind.edtKta.isError(kta)
        bind.edtNama.isError(name)
        bind.edtEmail.isError(email)
        bind.edtEmail.isValidEmail(email)
        bind.edtAddress.isError(address)
        bind.edtMobile.isError(mobile)
        bind.edtBirthplace.isError(birthplace)
        bind.edtBirthday.isError(birthday)
        bind.edtBirthmonth.isError(birthday)
        bind.edtBirthyear.isError(birthday)
        bind.edtPass.isError(password)

        val data = ProfilePost(
                kta, name, address, "",
                mobile, mobile, email,
                idProvince.toString(), idCity.toString(), idDistrict.toString(), village, "True", birthplace, "$birthyear-$birthmonth-$birthday", password)
        val gson = Gson()
        val rawFile = gson.toJson(data)
        Log.d("DATA", rawFile)

        AlertDialog.Builder(context)
                .setMessage("Apakah Anda yakin, periksa data yang anda masukkan")
                .setPositiveButton("Yes") { _, _ -> presenter.addAnggota(kta, rawFile) }
                .setNegativeButton("No", null)
                .show()
    }

    override fun profileSaved(responseCode: Int, id: String?) {
        if (responseCode == RegisterPresenter.REQUEST_SUCCESS) {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.view_container, VerificationFragment(), VerificationFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }
}