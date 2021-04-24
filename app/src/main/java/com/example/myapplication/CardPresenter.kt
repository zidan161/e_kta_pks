package com.example.myapplication

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.*
import retrofit2.http.*

class CardPresenter(private val view: CardView) {

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
                    .client(logging())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            return retrofit.create(ApiFun::class.java)
        }
    }

    fun getAnggota(id: String?) {
        val webService = DataRepository.create()
        webService.getAnggota("[('no_kta','=','$id')]").enqueue(object: Callback<GetResponse> {

            override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                if (response.body()!!.isSuccess) {
                    val data = response.body()!!.data[0]
                    view.setProfile(data)
                } else {
                    Log.e("INI YG ERROR", "ERROR")
                }
            }

            override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                Log.e("onFailure", "error", t)
            }
        })
    }

    interface ApiFun {
        @GET("api/res.partner/search?fields=['id','name','street','street2','desa_id','kecamatan_id','kota_id','propinsi_id','jenis_kel','tmp_lahir','tgl_lahir','mobile','email','no_kta','tgl_aktivasi','aktivasi_oleh']&limit=20")
        fun getAnggota(@Query("domain") domain: String): Call<GetResponse>
    }


    interface CardView {
        fun setProfile(profile: Profile)
    }
}