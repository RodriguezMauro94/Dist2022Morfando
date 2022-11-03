package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.uade.dist.morfando.R
import com.uade.dist.morfando.databinding.ActivityOwnerLoginBinding
import com.uade.dist.morfando.ui.viewmodel.OwnerLoginViewModel

const val OWNER_REGISTER_REQUEST_CODE = 1000

class OwnerLoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityOwnerLoginBinding
    private val ownerLoginViewModel: OwnerLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityOwnerLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.owner_login_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.login.setOnClickListener {
            ownerLoginViewModel.authenticate()
        }

        binding.register.setOnClickListener {
            ownerRegister()
        }

        binding.editTextEmail.addTextChangedListener {
            ownerLoginViewModel.email.postValue(it.toString())
        }

        binding.editTextPassword.addTextChangedListener {
            ownerLoginViewModel.password.postValue(it.toString())
        }

        ownerLoginViewModel.session.observe(this) { session ->
            if (session != null) {
                val intent = Intent()
                intent.putExtra("session", session)
                setResult(OWNER_LOGIN_REQUEST_CODE, intent)
                finish()
            }
        }

        binding.ownerForgotPassword.setOnClickListener {
            val intent = Intent(this, OwnerForgotPasswordActivity::class.java)
            this.startActivity(intent)
        }
    }

    private fun ownerRegister() {
        val intent = Intent(this, OwnerRegisterActivity::class.java)
        this.startActivityForResult(intent, OWNER_REGISTER_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OWNER_REGISTER_REQUEST_CODE) {
            // TODO finalizar con onresult y pasar el model del user
        }
    }
}