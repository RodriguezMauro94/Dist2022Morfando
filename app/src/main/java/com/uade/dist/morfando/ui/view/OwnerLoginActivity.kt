package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.databinding.ActivityOwnerLoginBinding
import com.uade.dist.morfando.ui.viewmodel.OwnerLoginViewModel

class OwnerLoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOwnerLoginBinding
    private val ownerLoginViewModel: OwnerLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.owner_login_title)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.login.setOnClickListener {
            // TODO
        }

        binding.register.setOnClickListener {
            // TODO
        }

        binding.ownerForgotPassword.setOnClickListener {
            // TODO
        }
    }
}