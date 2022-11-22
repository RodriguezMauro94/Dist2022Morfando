package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.databinding.ActivityOwnerRegisterBinding
import com.uade.dist.morfando.ui.viewmodel.OwnerRegisterViewModel

class OwnerRegisterActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOwnerRegisterBinding
    private val ownerRegisterViewModel: OwnerRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.owner_register_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.register.setOnClickListener {
            val email: String = binding.emailValue.text.toString()
            val password = binding.passwordValue.text.toString()
            val reenterPassword = binding.reenterPasswordValue.text.toString()
            val name: String = binding.nameValue.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() && reenterPassword.isNotEmpty()) {
                if (password == reenterPassword) {
                    ownerRegisterViewModel.register(email, password, name)
                } else {
                    getString(R.string.error_password_not_match).showToast(this)
                }
            } else {
                getString(R.string.error_complete_fields).showToast(this)
            }
        }

        ownerRegisterViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    val intent = Intent()
                    intent.putExtra("session", ownerRegisterViewModel.session)
                    setResult(OWNER_REGISTER_REQUEST_CODE, intent)
                    finish()
                }
                is RequestState.FAILURE -> {
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