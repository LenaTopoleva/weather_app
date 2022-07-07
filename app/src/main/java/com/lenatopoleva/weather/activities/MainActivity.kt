package com.lenatopoleva.weather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.lenatopoleva.weather.R
import com.lenatopoleva.weather.fragments.SecondFragment
import java.util.*

class MainActivity : AppCompatActivity() {
    var topAppBar: MaterialToolbar? = null
    val titleList: MutableList<String> = mutableListOf("Weather")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        topAppBar = findViewById(R.id.topAppBar)
        topAppBar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_location -> {
                    // Navigate to second fragment
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_view, SecondFragment.newInstance("Hello from FirstFragment"))
                        .addToBackStack(SecondFragment::class.java.simpleName)
                        .commit()
                    topAppBar?.title = "Second fragment"
                    titleList.add("Second fragment")
                    true
                }
                R.id.action_settings -> {
                    // Handle
                    true
                }
                else -> false
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            titleList.removeAt(titleList.size - 1)
            topAppBar?.title = titleList.last()
        } else {
            super.onBackPressed()
        }
    }
}