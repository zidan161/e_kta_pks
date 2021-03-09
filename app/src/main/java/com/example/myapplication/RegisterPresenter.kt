package com.example.myapplication

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

class RegisterPresenter(private val view: RegisterView) {

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

    fun addAnggota(id: String, data: String) {
        val webService = DataRepository.create()
        webService.createMember(data).enqueue(object: Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        view.profileSaved(REQUEST_SUCCESS, id)
                    } else {
                        view.profileSaved(REQUEST_FAILED, null)
                        Log.e("gagal", "${response.body()?.message}")
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e("error", "${t.message}", t)
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
        @POST("api/res.partner/create")
        fun createMember(@Body profile: String): Call<PostResponse>

        @GET("api/pks.propinsi/search?limit=50&fields=['id','name']")
        fun getProvince(): Call<Places>

        @GET("api/pks.kota/search?limit=100&fields=['id','name']&offset=0")
        fun getCity(@Query("domain") domain: String?): Call<Places>

        @GET("api/pks.kecamatan/search?limit=100&fields=['id','name']&offset=0")
        fun getDistrict(@Query("domain") domain: String?): Call<Places>

        @GET("api/pks.desa/search?fields=['id','name']&limit=100&offset=0")
        fun getVillage(@Query("domain") domain: String?): Call<Places>
    }

    interface RegisterView {
        fun profileSaved(responseCode: Int, id: String?)
        fun setProvince(province: List<Place>)
        fun setCity(city: List<Place>)
        fun setDistrict(district: List<Place>)
        fun setVillage(village: List<Place>?)
    }
}