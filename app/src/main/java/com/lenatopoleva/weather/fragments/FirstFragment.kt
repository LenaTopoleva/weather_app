package com.lenatopoleva.weather.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lenatopoleva.weather.CITY
import com.lenatopoleva.weather.R
import com.lenatopoleva.weather.REQUEST_KEY
import com.lenatopoleva.weather.adapters.WeatherListAdapter

class FirstFragment: Fragment() {
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Устанавливаем слушателя для получения данных из SecondFragment
        activity?.supportFragmentManager?.setFragmentResultListener(REQUEST_KEY, this) { _, result ->
            val chosenCity = result.getString(CITY)
            Log.d("CHOSEN_CITY", chosenCity ?: "null")
            Toast.makeText(requireContext(), chosenCity, Toast.LENGTH_LONG).show()
        }
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

    private fun initViews(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun setupRecyclerView(){
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = WeatherListAdapter()
    }
}