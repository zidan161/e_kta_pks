package com.example.myapplication

import android.content.Context
import android.util.Log
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class LoginPresenter(private var ctx: Context, private val view: LoginView) {

    companion object {
        const val REQUEST_AVAILABLE = 100
        const val REQUEST_NOT_AVAILABLE = 101
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
                .addConverterFactory(GsonConverterFactory.create())
                .client(logging())
                .build()
            return retrofit.create(ApiFun::class.java)
        }
    }

    fun getAnggota(number: String?, password: String?) {
        val webService = DataRepository.create()
        webService.getAnggota("[('mobile','=','$number')]").enqueue(object: Callback<GetResponse> {

            override fun onResponse(call: Call<GetResponse>, response: Response<GetResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.isSuccess) {
                        if (response.body()!!.data[0].activationDate != "false") {
                            if (response.body()!!.data[0].password == password) {
                                val data = response.body()!!.data[0]
                                view.isRegistered(REQUEST_AVAILABLE, data)
                            } else {
                                view.isRegistered(REQUEST_NOT_AVAILABLE, null)
                            }
                        } else {Toast.makeText(ctx, "Akun Anda belum aktif", Toast.LENGTH_SHORT).show()}
                    } else {Toast.makeText(ctx, "Data yang anda masukkan salah", Toast.LENGTH_SHORT).show()}
                }
            }

            override fun onFailure(call: Call<GetResponse>, t: Throwable) {
                Log.e("onFailure", "error", t)
            }
        })
    }

    interface ApiFun {
        @GET("api/res.partner/search?fields=['no_kta','id','name','street','street2','desa_id','kecamatan_id','kota_id','propinsi_id','jenis_kel','phone','mobile','email','passwd_kta','tgl_aktivasi','aktivasi_oleh']&limit=20")
        fun getAnggota(@Query("domain") domain: String): Call<GetResponse>
    }

    interface LoginView {
        fun isRegistered(requestCode: Int, data: Profile?)
    }
}