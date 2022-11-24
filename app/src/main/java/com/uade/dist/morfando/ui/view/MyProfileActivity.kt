package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.setVisibility
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_IS_OWNER
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_FAVOURITES
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.databinding.ActivityMyProfileBinding
import com.uade.dist.morfando.ui.viewmodel.MyProfileViewModel

class MyProfileActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMyProfileBinding
    private val myProfileViewModel: MyProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""
        val isOwner = sharedPreferences.getBoolean(SHARED_IS_OWNER, false)
        binding.profileMyRestaurants.setVisibility(isOwner)
        binding.profileChangePassword.setVisibility(isOwner)

        binding.profilePersonalData.setOnClickListener {
            startActivity(Intent(this, PersonalDataActivity::class.java))
        }

        binding.profileChangePassword.setOnClickListener {
            startActivity(Intent(this, OwnerChangePasswordActivity::class.java))
        }

        binding.profileMyRestaurants.setOnClickListener {
            startActivity(Intent(this, MyRestaurantsActivity::class.java))
        }

        binding.profileDeleteAccount.setOnClickListener {
            myProfileViewModel.deleteAccount(token)
        }

        binding.profileCloseSession.setOnClickListener {
            closeSession(sharedPreferences)
        }

        myProfileViewModel.deleteAccountRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    closeSession(sharedPreferences)
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }
    }

    private fun closeSession(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN, null).apply()
        sharedPreferences.edit().putString(SHARED_PREFERENCES_FAVOURITES, null).apply()

        val intent = Intent(this, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}