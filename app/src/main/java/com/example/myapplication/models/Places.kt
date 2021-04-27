package com.example.myapplication.models

import com.google.gson.annotations.SerializedName

data class Places(@SerializedName("data") var data: List<Place>)

data class Place(@SerializedName("id") val id: Int?, @SerializedName("name") val name: String?){
    override fun toString(): String {
        return name!!
    }
}