package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.databinding.ActivityRestaurantDetailsBinding
import com.uade.dist.morfando.ui.viewmodel.RestaurantDetailsViewModel

class RestaurantDetailsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantDetailsBinding
    private val restaurantDetailsViewModel: RestaurantDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
}