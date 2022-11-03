package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.databinding.ActivityLoginBinding
import com.uade.dist.morfando.ui.viewmodel.LoginViewModel

const val GOOGLE_SIGN_IN_REQUEST_CODE = 1000
const val OWNER_LOGIN_REQUEST_CODE = 2000

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //TODO .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.loginGoogle.setOnClickListener {
            googleSignIn()
        }

        binding.loginOwner.setOnClickListener {
            ownerLogin()
        }
    }

    private fun ownerLogin() {
        val intent = Intent(this, OwnerLoginActivity::class.java)
        this.startActivityForResult(intent, OWNER_LOGIN_REQUEST_CODE)
    }

    private fun googleSignIn() {
        val intent = googleSignInClient.signInIntent
        this.startActivityForResult(intent, GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                if (task.isComplete && !task.isSuccessful) {
                    // User canceled the login
                    return
                } else {
                    val result = task.getResult(ApiException::class.java)
                    result?.apply {
                        //FIXME loginSuccess(this.id!!)
                        loginSuccess("1234")
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.generic_error), Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == OWNER_LOGIN_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("session") as SessionModel
                loginSuccess(result.session)
            }
        }
    }

    private fun loginSuccess(id: String) {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        loginViewModel.loginSuccess(sharedPreferences, id)
        startActivity(Intent(this@LoginActivity, RequestGeoPermissionActivity::class.java))
        finish()
    }
}