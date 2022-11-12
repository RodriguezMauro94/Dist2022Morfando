package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.OpenHoursModel
import com.uade.dist.morfando.databinding.ActivityOpenHoursBinding

class OpenHoursActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOpenHoursBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityOpenHoursBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val openHours = intent.extras?.getSerializable("openHours") as OpenHoursModel

        binding.openHoursMondayValue.text = getOpenHours(openHours.monday.openHours, openHours.monday.closeHours, openHours.monday.isOpen)
        binding.openHoursTuesdayValue.text = getOpenHours(openHours.tuesday.openHours, openHours.tuesday.closeHours, openHours.tuesday.isOpen)
        binding.openHoursWednesdayValue.text = getOpenHours(openHours.wednesday.openHours, openHours.wednesday.closeHours, openHours.wednesday.isOpen)
        binding.openHoursThursdayValue.text = getOpenHours(openHours.thursday.openHours, openHours.thursday.closeHours, openHours.thursday.isOpen)
        binding.openHoursFridayValue.text = getOpenHours(openHours.friday.openHours, openHours.friday.closeHours, openHours.friday.isOpen)
        binding.openHoursSaturdayValue.text = getOpenHours(openHours.saturday.openHours, openHours.saturday.closeHours, openHours.saturday.isOpen)
        binding.openHoursSundayValue.text = getOpenHours(openHours.sunday.openHours, openHours.sunday.closeHours, openHours.sunday.isOpen)
    }

    private fun getOpenHours(openHours: String?, closeHours: String?, isOpen: Boolean): String {
        return if (isOpen) {
            getString(R.string.open_hours_template, openHours, closeHours)
        } else {
            getString(R.string.closed)
        }

    }
}