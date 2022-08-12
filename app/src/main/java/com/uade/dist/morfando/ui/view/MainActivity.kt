package com.uade.dist.morfando.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.uade.dist.morfando.databinding.ActivityMainBinding
import com.uade.dist.morfando.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.onCreate()

        mainViewModel.restaurantModel.observe(this, Observer {
            binding.landing.text = it.name + " + " + it.speciality
        })

        binding.landing.setOnClickListener {
            mainViewModel.restaurantDetails()
        }
    }
}