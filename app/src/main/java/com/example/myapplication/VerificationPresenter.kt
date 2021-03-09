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

class VerificationPresenter(private val view: VerifyView) {

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

    fun postProfile(id: String, data: String){
        val webService = DataRepository.create()
        webService.createMember(data).enqueue(object : Callback<PostResponse> {
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

    fun getAnggota(phone: String?, requestCode: String) {
        val webService = DataRepository.create()
        webService.getMember("[('mobile','=','$phone')]").enqueue(object : Callback<GetResponse> {

            override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        if (requestCode == VerificationFragment.REQUEST_PASSWORD) {
                            val data = response.body()!!.data[0].id
                            view.setProfile(data)
                        } else {
                            val data = response.body()!!.data[0].kta!!
                            view.login(data)
                        }
                    } else {
                        Log.e("INI YG ERROR", "Verify")
                    }
                }
            }

            override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                Log.e("onFailure", "error", t)
            }
        })
    }

    interface ApiFun {
        @POST("api/res.partner/create")
        fun createMember(@Body profile: String): Call<PostResponse>

        @GET("api/res.partner/search?fields=['id','name','street','street2','desa_id','kecamatan_id','kota_id','propinsi_id','jenis_kel','phone','mobile','email']&limit=20")
        fun getMember(@Query("domain") domain: String): Call<GetResponse>
    }

    interface VerifyView {
        fun profileSaved(requestCode: Int, id: String?)
        fun setProfile(id: String)
        fun login(id: String)
    }
}