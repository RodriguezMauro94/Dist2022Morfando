package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
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
        supportActionBar?.setHomeButtonEnabled(true)

        binding.register.setOnClickListener {
            // TODO
        }
    }
}