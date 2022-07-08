package com.lenatopoleva.weather.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lenatopoleva.weather.R
import com.lenatopoleva.weather.data.WeatherList
import com.lenatopoleva.weather.data.WeatherResponse
import com.lenatopoleva.weather.data.WeatherTest

class WeatherListAdapter: RecyclerView.Adapter<WeatherListAdapter.WeatherListViewHolder>() {

    var weatherList: List<WeatherList> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
         field = value
         this.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return WeatherListViewHolder((itemView))
    }

    override fun onBindViewHolder(holder: WeatherListViewHolder, position: Int) {
       holder.day?.text = "today"
       holder.temperature?.text = weatherList[position].main?.temp.toString()
    }

    override fun getItemCount(): Int = weatherList.size

    class WeatherListViewHolder(view: View): RecyclerView.ViewHolder(view){
        val day: TextView? = view.findViewById(R.id.dayTextView)
        val temperature: TextView? = view.findViewById(R.id.temperatureTextView)
    }
}