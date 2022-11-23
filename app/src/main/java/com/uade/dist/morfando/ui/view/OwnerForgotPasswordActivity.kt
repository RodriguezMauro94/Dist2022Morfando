package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.databinding.ActivityOwnerForgotPasswordBinding
import com.uade.dist.morfando.ui.viewmodel.OwnerForgotPasswordViewModel

class OwnerForgotPasswordActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOwnerForgotPasswordBinding
    private val ownerRegisterViewModel: OwnerForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.owner_forgot_password_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.retrieve.setOnClickListener {
            // TODO forgot password
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}