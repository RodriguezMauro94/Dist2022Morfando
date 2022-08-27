package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.databinding.ActivityLoginBinding
import com.uade.dist.morfando.ui.view.home.HomeActivity
import com.uade.dist.morfando.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val homeViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.onCreate()

        homeViewModel.restaurantModel.observe(this) {
            binding.landing.text = it.name + " + " + it.speciality
        }

        binding.landing.setOnClickListener {
            /*homeViewModel.restaurantDetails()
            startActivity(Intent(this, MapActivity::class.java))*/

            GlobalScope.launch {
                val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN, "1234").apply() //FIXME deshardcodear
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            }
        }
    }
}