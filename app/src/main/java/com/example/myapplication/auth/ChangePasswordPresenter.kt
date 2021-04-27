package com.example.myapplication.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.myapplication.models.GetResponse
import com.example.myapplication.models.PostResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

class ChangePasswordPresenter(private val ctx: Context, private val view: PasswordView) {

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

    fun checkAnggota(phone: String?) {
        val webService = DataRepository.create()
        webService.getAnggota("[('mobile','=','$phone')]").enqueue(object: Callback<GetResponse> {

            override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        val data = response.body()!!.data[0].id
                        Toast.makeText(ctx, "Nomor telepon terdaftar", Toast.LENGTH_SHORT).show()
                        view.isRegistered(data)
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

    fun changePassword(id: String?, data: String?) {
        val webService = DataRepository.create()
        webService.updatePass(id, data).enqueue(object: Callback<PostResponse> {

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        view.passwordSaved()
                    } else {
                        Log.e("Error", "GANTI PASSWORD")
                    }
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.e("onFailure", "error", t)
            }
        })
    }

    interface ApiFun {
        @GET("api/res.partner/search?fields=['id','name','street','street2','no_kta']&limit=20")
        fun getAnggota(@Query("domain") domain: String): Call<GetResponse>

        @PUT("api/res.partner/{id}")
        fun updatePass(@Path("id") id: String?, @Body data: String?): Call<PostResponse>
    }

    interface PasswordView {
        fun isRegistered(id: String?)
        fun passwordSaved()
    }
}