package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.databinding.ActivityLoginBinding
import com.uade.dist.morfando.ui.view.home.HomeActivity
import com.uade.dist.morfando.ui.viewmodel.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val homeViewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.onCreate()

        homeViewModel.restaurantModel.observe(this) {
            binding.landing.text = it.name + " + " + it.speciality
        }

        binding.landing.setOnClickListener {
            /*homeViewModel.restaurantDetails()
            startActivity(Intent(this, MapActivity::class.java))*/

            GlobalScope.launch {
                val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
                sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN, "1234").apply() //FIXME deshardcodear
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()
            }
        }


        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //TODO .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.loginGoogle.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val intent = googleSignInClient.signInIntent
        this.startActivityForResult(intent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val result = task.getResult(ApiException::class.java)

                loginSuccess(result)
            } catch (e: Exception) {
                Toast.makeText(this, "Algo salió mal, vuelve a intentarlo en unos instantes", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun loginSuccess(account: GoogleSignInAccount?) {
        account?.run {
            Log.d("account- displayName", this.displayName!!)
            Log.d("account- familyName", this.familyName!!)
            Log.d("account- givenName", this.givenName!!)
            Log.d("account- email", this.email!!)
            Log.d("account- id", this.id!!)
            Log.d("account- id", this.idToken!!) // TODO enviar id a backend y validarlo https://developers.google.com/identity/sign-in/android/backend-auth

            val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN, "1234").apply() //FIXME enviar idToken
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }
}