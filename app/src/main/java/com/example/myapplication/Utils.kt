package com.example.myapplication

import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import android.widget.EditText
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

fun EditText.isError(value: String){
    if (value.isEmpty()){
        error = "Field ini harus diisi!"
        return
    }
}

fun EditText.isValidEmail(value: String){
    Patterns.EMAIL_ADDRESS.matcher(value).matches()
    error = "Email tidak valid!"
    return
}

@Suppress("DEPRECATION")
fun isNetworkAvailable(ctx: Context): Boolean {
    val conManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val internetInfo = conManager.activeNetworkInfo
    var state = false
    if (internetInfo != null) {
        if (internetInfo.isConnected && isOnline()) {
            state = true
        }
    }
    return state
}

fun isOnline(): Boolean {
    val result: Deferred<Boolean> = GlobalScope.async(Dispatchers.IO) {
        try {
            val timeoutMs = 1500
            val sock = Socket()
            val sockaddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            sock.connect(sockaddr, timeoutMs)
            sock.close()

            true
        } catch (e: IOException) {
            false
        }
    }

    var finalResult = false
    runBlocking {
        finalResult = result.await()
    }
    return finalResult
}