package com.lenatopoleva.weather.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.lenatopoleva.weather.CITY
import com.lenatopoleva.weather.R
import com.lenatopoleva.weather.REQUEST_KEY
import com.lenatopoleva.weather.adapters.WeatherListAdapter
import com.lenatopoleva.weather.data.WeatherResponse
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class FirstFragment : Fragment() {
    var recyclerView: RecyclerView? = null
    var city: TextView? = null
    var temperature: TextView? = null
    var pressure: TextView? = null
    var responseCode: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Устанавливаем слушателя для получения данных из SecondFragment
        activity?.supportFragmentManager?.setFragmentResultListener(
            REQUEST_KEY,
            this
        ) { _, result ->
            val chosenCity = result.getString(CITY)
            Log.d("CHOSEN_CITY", chosenCity ?: "null")
            Toast.makeText(requireContext(), chosenCity, Toast.LENGTH_LONG).show()
            // Create weather request
            chosenCity?.let { loadWeatherData(it) }
        }
    }

    private fun loadWeatherData(chosenCity: String) {
        val url = URL(createWeatherUrl(chosenCity))
        // Запоминаем главный поток
        val handler = Handler(Looper.getMainLooper())
        Thread {
            var urlConnection: HttpsURLConnection? = null
            try {
                urlConnection = url.openConnection() as? HttpsURLConnection
                urlConnection?.requestMethod = "GET"
                urlConnection?.connectTimeout = 10000
                responseCode = urlConnection?.responseCode
                val bufferedReader: BufferedReader =
                    BufferedReader(InputStreamReader(urlConnection?.inputStream))
                val result = getLines(bufferedReader)
                val gson = Gson()
                val weatherResponse = gson.fromJson(result, WeatherResponse::class.java)
                // Возвращаемся к основному потоку
                handler.post {
                    Log.d("network", "showWeather")
                    showWeather(weatherResponse)
                }
            } catch (e: Exception) {
                Log.d("network", "responseCode = $responseCode")
                Log.e("network", e.message.toString())
                handler.post {
                    checkResponseCode(responseCode)
                }
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }
        }.start()
    }

    private fun checkResponseCode(responseCode: Int?) {
        when {
            responseCode == 404 -> {
                Log.d("network", "responseCode = $responseCode")
                Toast.makeText(context, "City not found", Toast.LENGTH_LONG).show()
            }
            responseCode != 200 -> {
                Log.d("network", "responseCode = $responseCode")
                Toast.makeText(context, "Fail connection", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getLines(reader: BufferedReader): String {
        val rawData = StringBuilder(1024)
        var tempVariable: String?
        while (true) {
            try {
                tempVariable = reader.readLine()
                if (tempVariable == null) break
                rawData.append(tempVariable).append("\n")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        try {
            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rawData.toString()
    }

    private fun showWeather(weatherResponse: WeatherResponse) {
        city?.text = weatherResponse.city?.name
        val temp = weatherResponse.list.first().main?.temp
        temp?.let { temperature?.text = Math.round(temp).toString() }
        pressure?.text = weatherResponse.list.first().main?.pressure.toString()

        (recyclerView?.adapter as? WeatherListAdapter)?.weatherList = weatherResponse.list
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupRecyclerView()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        city = view.findViewById(R.id.city)
        temperature = view.findViewById(R.id.temperature)
        pressure = view.findViewById(R.id.pressure)
    }

    private fun setupRecyclerView() {
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = WeatherListAdapter()
    }

    fun createWeatherUrl(city: String) =
        "$BASE_URL$ROAD?$CITY_PARAMETER$city&$UNITS_PARAMETER&$KEY_PARAMETER$API_KEY"

    companion object {
        const val BASE_URL = "https://api.openweathermap.org"
        const val ROAD = "/data/2.5/forecast"
        const val CITY_PARAMETER = "q="
        const val UNITS_PARAMETER = "units=metric"
        const val KEY_PARAMETER = "appid="
        const val API_KEY = "c050dd71549bafa782c25c077c1a3a47"
    }
}