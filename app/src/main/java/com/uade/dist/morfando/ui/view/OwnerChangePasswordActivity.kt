package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.databinding.ActivityOwnerChangePasswordBinding
import com.uade.dist.morfando.ui.viewmodel.OwnerChangePasswordViewModel

class OwnerChangePasswordActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOwnerChangePasswordBinding
    private val ownerChangePasswordViewModel: OwnerChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        binding.save.setOnClickListener {
            val password = binding.passwordValue.text.toString()
            val reenterPassword = binding.reenterPasswordValue.text.toString()

            if (password.isNotEmpty() && reenterPassword.isNotEmpty()) {
                if (password == reenterPassword) {
                    ownerChangePasswordViewModel.changePassword(token, password)
                } else {
                    getString(R.string.error_password_not_match).showToast(this)
                }
            } else {
                getString(R.string.error_complete_fields).showToast(this)
            }
        }

        ownerChangePasswordViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.password_updated).showToast(this)
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }
    }
}