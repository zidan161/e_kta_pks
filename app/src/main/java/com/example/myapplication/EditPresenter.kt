package com.example.myapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

class EditPresenter(private val ctx: Context, private val view: EditView) {

    companion object {
        const val REQUEST_SUCCESS = 100
        const val REQUEST_FAILED = 101
    }

    object DataRepository {
        private fun logging(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(Interceptor { chain ->
                val request = chain.request()
                        .newBuilder()
                        .addHeader("api_key", "67BGOZV48SVUIJLH1ZHIWGNWT6PU32MY")
                        .build()
                chain.proceed(request)
            })
            return builder.build()
        }

        fun create(): ApiFun {
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://13.212.79.148:8069/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(logging())
                    .build()
            return retrofit.create(ApiFun::class.java)
        }
    }

    fun updateAnggota(id: String, data: String) {
        val webService = DataRepository.create()
        webService.updateAnggota(id, data).enqueue(object: Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        view.profileUpdated(REQUEST_SUCCESS, id)
                    } else {
                        view.profileUpdated(REQUEST_FAILED, null)
                        Log.e("gagal", "${response.body()?.message}")
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e("error", "${t.message}", t)
            }
        })
    }

    fun getAnggota(id: String) {
        val webService = DataRepository.create()
        webService.getAnggota("[('no_kta','=','$id')]").enqueue(object: Callback<GetResponse> {

            override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        val data = response.body()!!.data[0]
                        view.setAnggota(data)
                    } else {
                        Toast.makeText(ctx, "Nomor telepon tidak terdaftar", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                Log.e("onFailure", "error", t)
            }
        })
    }

    fun getProvince(){
        val webService = DataRepository.create()
        webService.getProvince().enqueue(object : Callback<Places> {
            override fun onResponse(call: Call<Places>, response: Response<Places>) {
                if (response.isSuccessful) {
                    view.setProvince(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<Places>, t: Throwable) {
                Log.e("error", "${t.message}", t)
            }
        })
    }

    fun getCity(id: Long){
        val webService = DataRepository.create()
        webService.getCity("[('propinsi_id.id','=',$id)]").enqueue(object : Callback<Places> {
            override fun onResponse(call: Call<Places>, response: Response<Places>) {
                if (response.isSuccessful) {
                    view.setCity(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<Places>, t: Throwable) {
                Log.e("error", "error", t)
            }
        })
    }

    fun getDistrict(id: Long){
        val webService = DataRepository.create()
        webService.getDistrict("[('kota_id.id','=',$id)]").enqueue(object : Callback<Places> {
            override fun onResponse(call: Call<Places>, response: Response<Places>) {
                if (response.isSuccessful) {
                    view.setDistrict(response.body()!!.data)
                }
            }

            override fun onFailure(call: Call<Places>, t: Throwable) {
                Log.e("error", "error", t)
            }
        })
    }

    fun getVillage(id: Long){
        val webService = DataRepository.create()
        webService.getVillage("[('kecamatan_id.id','=',$id)]").enqueue(object : Callback<Places> {
            override fun onResponse(call: Call<Places>, response: Response<Places>) {
                view.setVillage(response.body()!!.data)
            }

            override fun onFailure(call: Call<Places>, t: Throwable) {
                Log.e("error", "error", t)
            }
        })
    }

    interface ApiFun {
        @GET("api/res.partner/search?fields=['no_kta','id','name','street','street2','desa_id','kecamatan_id','kota_id','propinsi_id','jenis_kel','phone','mobile','email','passwd_kta','tgl_aktivasi','aktivasi_oleh','tmp_lahir','tgl_lahir']&limit=20")
        fun getAnggota(@Query("domain") domain: String): Call<GetResponse>

        @PUT("api/res.partner/{id}")
        fun updateAnggota(@Path("id") id: String, @Body profile: String): Call<PostResponse>

        @GET("api/pks.propinsi/search?limit=50&fields=['id','name']")
        fun getProvince(): Call<Places>

        @GET("api/pks.kota/search?limit=100&fields=['id','name']&offset=0")
        fun getCity(@Query("domain") domain: String?): Call<Places>

        @GET("api/pks.kecamatan/search?limit=100&fields=['id','name']&offset=0")
        fun getDistrict(@Query("domain") domain: String?): Call<Places>

        @GET("api/pks.desa/search?fields=['id','name']&limit=100&offset=0")
        fun getVillage(@Query("domain") domain: String?): Call<Places>
    }

    interface EditView {
        fun setAnggota(data: Profile)
        fun profileUpdated(responseCode: Int, id: String?)
        fun setProvince(province: List<Place>)
        fun setCity(city: List<Place>)
        fun setDistrict(district: List<Place>)
        fun setVillage(village: List<Place>?)
    }
}