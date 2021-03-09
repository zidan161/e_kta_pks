package com.example.myapplication

import android.widget.EditText

fun EditText.isError(value: String){
    if (value.isEmpty()){
        error = "Field ini harus diisi!"
    }
}