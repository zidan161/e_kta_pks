package com.example.myapplication

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event(var url: String,
                 var name: String,
                 var desc: String,
                 var image: String): Parcelable
