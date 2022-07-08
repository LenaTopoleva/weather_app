package com.lenatopoleva.weather.data

import com.google.gson.annotations.SerializedName

data class Clouds (
    @SerializedName("all" ) var all : Int? = null
)