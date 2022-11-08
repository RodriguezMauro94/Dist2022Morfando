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

        binding.openHoursMondayValue.text = openHours.monday.openHours
        binding.openHoursTuesdayValue.text = openHours.tuesday.openHours
        binding.openHoursWednesdayValue.text = openHours.wednesday.openHours
        binding.openHoursThursdayValue.text = openHours.thursday.openHours
        binding.openHoursFridayValue.text = openHours.friday.openHours
        binding.openHoursSaturdayValue.text = openHours.saturday.openHours
        binding.openHoursSundayValue.text = openHours.sunday.openHours
    }
}