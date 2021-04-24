package com.example.myapplication

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class GetResponse(
        @SerializedName("success")
        val isSuccess: Boolean,
        @SerializedName("data")
        val data: List<Profile>
)

data class PostResponse(
        @SerializedName("success")
        val isSuccess: Boolean,
        @SerializedName("message")
        val message: String?
)

data class Profile(
        @SerializedName("id")
        val id: String,
        @SerializedName("no_kta")
        val kta: String?,
        @SerializedName("name")
        val name: String,
        @SerializedName("street")
        val street: String,
        @SerializedName("street2")
        val street2: String,
        @SerializedName("phone")
        val phone: String?,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("propinsi_id")
        val province: List<Place>,
        @SerializedName("kota_id")
        val city: List<Place>,
        @SerializedName("kecamatan_id")
        val district: List<Place>,
        @SerializedName("desa_id")
        val village: List<Place>,
        @SerializedName("jenis_kel")
        val gender: String,
        @SerializedName("tmp_lahir")
        val birthPlace: String,
        @SerializedName("tgl_lahir")
        val birthDay: String,
        @SerializedName("passwd_kta")
        val password: String,
        @SerializedName("tgl_aktivasi")
        val activationDate: String?,
        @SerializedName("aktivasi_oleh")
        val activationBy: List<String?>?
)

@Parcelize
data class ProfilePost(
        @SerializedName("no_kta")
        var kta: String?,
        @SerializedName("name")
        var name: String,
        @SerializedName("street")
        var street: String,
        @SerializedName("street2")
        var street2: String,
        @SerializedName("phone")
        var phone: String?,
        @SerializedName("mobile")
        var mobile: String,
        @SerializedName("email")
        var email: String,
        @SerializedName("propinsi_id")
        var province: String,
        @SerializedName("kota_id")
        var city: String,
        @SerializedName("kecamatan_id")
        var district: String,
        @SerializedName("desa_id")
        var village: String,
        @SerializedName("anggota")
        var anggota: String,
        @SerializedName("tmp_lahir")
        var birthPlace: String?,
        @SerializedName("tgl_lahir")
        var birthDay: String?,
        @SerializedName("passwd_kta")
        var password: String?
): Parcelable