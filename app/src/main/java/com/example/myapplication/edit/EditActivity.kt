package com.example.myapplication.edit

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.myapplication.models.Place
import com.example.myapplication.models.Profile
import com.example.myapplication.databinding.ActivityEditBinding
import com.example.myapplication.isError
import com.example.myapplication.isValidEmail
import com.example.myapplication.main.CustomArrayAdapter
import com.google.gson.Gson

class EditActivity : AppCompatActivity(), EditPresenter.EditView {

    companion object {
        const val ID = "kta_edit"
    }

    private lateinit var binding: ActivityEditBinding
    private lateinit var presenter: EditPresenter

    private var idProvince: Long? = null
    private var idCity: Long? = null
    private var idDistrict: Long? = null
    private var idVillage: Long? = null
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val kta = intent.getStringExtra(ID)!!

        presenter = EditPresenter(this, this)
        presenter.getAnggota(kta)
        presenter.getProvince()

        binding.btnUpdate.setOnClickListener {
            val name = binding.edtNama.text.toString()
            val email = binding.edtEmail.text.toString().trim()
            val birthPlace = binding.edtBirthplace.text.toString()
            val birthday = binding.edtBirthday.text.toString().trim()
            val birthmonth = binding.edtBirthmonth.text.toString().trim()
            val birthyear = binding.edtBirthyear.text.toString().trim()
            val address = binding.edtAddress.text.toString()
            val phone = binding.edtPhone.text.toString()
            val mobile = binding.edtMobile.text.toString()
            val village = if (idVillage == null) {
                ""
            } else {
                idVillage.toString()
            }

            binding.edtNama.isError(name)
            binding.edtEmail.isError(email)
            binding.edtMobile.isError(mobile)
            binding.edtPhone.isError(phone)
            binding.edtBirthplace.isError(birthPlace)
            binding.edtBirthday.isError(birthday)
            binding.edtBirthmonth.isError(birthmonth)
            binding.edtBirthyear.isError(birthyear)
            binding.edtAddress.isError(address)

            binding.edtEmail.isValidEmail(email)

            val data: MutableMap<String, String> = mutableMapOf()

            data["name"] = name
            data["email"] = email
            data["street"] = address
            data["mobile"] = mobile
            data["phone"] = phone
            data["tmp_lahir"] = birthPlace
            data["tgl_lahir"] = "$birthyear-$birthmonth-$birthday"
            if (idProvince != null) {
                data["propinsi_id"] = idProvince.toString()
                data["kota_id"] = idCity.toString()
                data["kecamatan_id"] = idDistrict.toString()
                data["desa_id"] = village
            }

            val raw = Gson().toJson(data)

            AlertDialog.Builder(this)
                    .setMessage("Apakah Anda yakin, periksa data yang anda masukkan")
                    .setPositiveButton("Yes") { _, _ -> presenter.updateAnggota(id, raw) }
                    .setNegativeButton("No", null)
                    .show()
        }
    }

    override fun setAnggota(data: Profile) {
        val date = data.birthDay.split("-").toTypedArray()
        id = data.id
        binding.edtNama.setText(data.name)
        binding.edtEmail.setText(data.email)
        binding.edtBirthplace.setText(data.birthPlace)
        binding.edtBirthday.setText(date[2])
        binding.edtBirthmonth.setText(date[1])
        binding.edtBirthyear.setText(date[0])
        binding.edtAddress.setText(data.street)
        binding.edtProvince.setText(data.province[0].name)
        binding.edtCity.setText(data.city[0].name)
        binding.edtDistrict.setText(data.district[0].name)
        binding.edtKta.setText(data.kta)
        binding.edtPhone.setText(data.phone)
        binding.edtMobile.setText(data.mobile)
        if (data.province[0].id!! == 49){
            binding.tfVillage.visibility = View.VISIBLE
            binding.edtVillage.setText(data.village[0].name)
        }
    }

    override fun profileUpdated(responseCode: Int, id: String?) {
        if (responseCode == EditPresenter.REQUEST_SUCCESS) {
            Toast.makeText(this, "Berhasil diedit", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun setProvince(province: List<Place>) {
        val adapter = CustomArrayAdapter(this, province)
        (binding.tfProvince.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getCity(id)
                binding.edtCity.text = null
                binding.edtDistrict.text = null
                binding.edtVillage.text = null
                if (id == 49L){
                    binding.tfVillage.visibility = View.VISIBLE
                    idProvince = id
                } else {
                    binding.tfVillage.visibility = View.GONE
                    idProvince = null
                }
            }
        }
    }

    override fun setCity(city: List<Place>) {
        val adapter = CustomArrayAdapter(this, city)
        (binding.tfCity.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getDistrict(id)
                idCity = id
                binding.edtDistrict.text = null
                binding.edtVillage.text = null
            }
        }
    }

    override fun setDistrict(district: List<Place>) {
        val adapter = CustomArrayAdapter(this, district)
        (binding.tfDistrict.editText as AutoCompleteTextView).apply {
            setAdapter(adapter)
            onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id ->
                presenter.getVillage(id)
                idDistrict = id
                binding.edtVillage.text = null
            }
        }
    }

    override fun setVillage(village: List<Place>?) {
        if (village != null) {
            val adapter = CustomArrayAdapter(this, village)
            (binding.tfVillage.editText as AutoCompleteTextView).apply {
                setAdapter(adapter)
                onItemClickListener = AdapterView.OnItemClickListener { _, _, _, id -> idVillage = id }
            }
            binding.edtVillage.inputType = InputType.TYPE_NULL
        } else {
            binding.edtVillage.inputType = InputType.TYPE_CLASS_TEXT
        }
    }
}