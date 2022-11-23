package com.uade.dist.morfando.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
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

        binding.forgotPassword.setOnClickListener {
            val email = binding.emailValue.text.toString()
            if (email.isNotEmpty()) {
                ownerRegisterViewModel.forgotPassword(email)
            }
        }

        binding.validate.setOnClickListener {
            val email = binding.emailValue.text.toString()
            val otpCode = binding.otpCodeValue.text.toString()
            if (email.isNotEmpty() && otpCode.isNotEmpty()) {
                ownerRegisterViewModel.validateOtp(email, otpCode)
            }
        }

        ownerRegisterViewModel.requestForgotPasswordState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.emailValue.isEnabled = false
                    binding.otpCodeValue.isEnabled = false
                    binding.validate.visibility = View.GONE
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    binding.emailValue.isEnabled = false
                    binding.otpCodeValue.isEnabled = true
                    binding.textInputLayoutOtp.visibility = View.VISIBLE
                    binding.validate.visibility = View.VISIBLE
                    binding.forgotPassword.visibility = View.GONE
                    getString(R.string.insert_otp_code).showToast(this)
                }
                is RequestState.FAILURE -> {
                    binding.emailValue.isEnabled = true
                    binding.otpCodeValue.isEnabled = false
                    binding.textInputLayoutOtp.visibility = View.GONE
                    binding.validate.visibility = View.GONE
                    binding.forgotPassword.visibility = View.VISIBLE
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        ownerRegisterViewModel.requestValidateOtpState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.emailValue.isEnabled = false
                    binding.otpCodeValue.isEnabled = false
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.otp_success).showToast(this)
                    finish()
                }
                is RequestState.FAILURE -> {
                    binding.emailValue.isEnabled = false
                    binding.otpCodeValue.isEnabled = true
                    binding.forgotPassword.visibility = View.GONE
                    binding.validate.visibility = View.VISIBLE
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}