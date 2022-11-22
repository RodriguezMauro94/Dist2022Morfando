package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.initCloudinary
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_IS_OWNER
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.ui.view.home.HomeActivity
import com.uade.dist.morfando.ui.viewmodel.LoginViewModel
import com.uade.dist.morfando.ui.viewmodel.SplashViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Splash)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initCloudinary(this)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null)
        if (token == null) {
            goToLogin()
        } else {
            splashViewModel.getPersonalData(sharedPreferences, token)
        }

        splashViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                    finish()
                }
                is RequestState.FAILURE -> {
                    goToLogin()
                }
            }
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }
}