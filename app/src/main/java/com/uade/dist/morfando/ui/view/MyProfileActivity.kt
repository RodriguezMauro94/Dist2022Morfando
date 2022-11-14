package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.setVisibility
import com.uade.dist.morfando.data.local.SHARED_IS_OWNER
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.databinding.ActivityMyProfileBinding

class MyProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val isOwner = sharedPreferences.getBoolean(SHARED_IS_OWNER, false)
        binding.profileMyRestaurants.setVisibility(isOwner)

        binding.profilePersonalData.setOnClickListener {
            startActivity(Intent(this, PersonalDataActivity::class.java))
        }

        binding.profileChangePassword.setOnClickListener {
            // TODO
        }

        binding.profileMyRestaurants.setOnClickListener {
            startActivity(Intent(this, MyRestaurantsActivity::class.java))
        }

        binding.profileDeleteAccount.setOnClickListener {
            // TODO
        }
    }
}