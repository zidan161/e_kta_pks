package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
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

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}


fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
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