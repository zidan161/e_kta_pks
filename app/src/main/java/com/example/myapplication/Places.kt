package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class Places(@SerializedName("data") var data: List<Place>)

data class MemberPlace(var data: List<Place>)

data class Place(@SerializedName("id") val id: Int?,@SerializedName("name") val name: String?){
    override fun toString(): String {
        return name!!
    }
}