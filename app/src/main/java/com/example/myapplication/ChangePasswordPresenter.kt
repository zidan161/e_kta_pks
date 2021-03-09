package com.example.myapplication

import android.util.Log
import com.google.gson.annotations.SerializedName
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

class ChangePasswordPresenter(private val view: PasswordView) {

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

    fun changePassword(id: String?, password: String?) {
        val webService = DataRepository.create()
        webService.updatePass(id, password).enqueue(object: Callback<PostResponse> {

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
        @PUT("/api/res.partner/{ID}")
        fun updatePass(@Path("ID") id: String?, @Body pass: String?): Call<PostResponse>
    }

    interface PasswordView {
        fun passwordSaved()
    }
}