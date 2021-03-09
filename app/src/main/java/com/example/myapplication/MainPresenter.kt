package com.example.myapplication

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.*
import retrofit2.http.*

class MainPresenter(private val view: MainView) {

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

    fun getNews() {
        val webService = DataRepository.create()
        webService.getNews().enqueue(object : Callback<List<String>> {

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null) {
                        view.setNews(data)
                    }
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.e("onFailure", "error", t)
            }

        })
    }

    interface ApiFun {

        @GET("news")
        fun getNews(): Call<List<String>>
    }


    interface MainView {
        fun setNews(data: List<String>)
    }
}