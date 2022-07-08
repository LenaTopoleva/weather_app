package com.lenatopoleva.weather.data

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Int? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var list: ArrayList<WeatherList> = arrayListOf(),
    @SerializedName("city") var city: City? = City()
)