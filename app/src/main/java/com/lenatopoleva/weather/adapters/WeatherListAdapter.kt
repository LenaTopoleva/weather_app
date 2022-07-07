package com.lenatopoleva.weather.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lenatopoleva.weather.R
import com.lenatopoleva.weather.data.Weather

class WeatherListAdapter: RecyclerView.Adapter<WeatherListAdapter.WeatherListViewHolder>() {

    private val weatherList: List<Weather> = listOf<Weather>(
        Weather("Monday", "+30 °C"),
        Weather("Tuesday", "+30 °C"),
        Weather("Wednesday", "+10 °C"),
        Weather("Thursday", "+10 °C"),
        Weather("Friday", "+10 °C"),
        Weather("Saturday", "+30 °C"),
        Weather("Sunday", "+30 °C")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return WeatherListViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
       holder.day?.text = weatherList[position].day
       holder.temperature?.text = weatherList[position].temperature
    }

    override fun getItemCount(): Int = weatherList.size

    class WeatherListViewHolder(view: View): RecyclerView.ViewHolder(view){
        val day: TextView? = view.findViewById(R.id.dayTextView)
        val temperature: TextView? = view.findViewById(R.id.temperatureTextView)
    }
}