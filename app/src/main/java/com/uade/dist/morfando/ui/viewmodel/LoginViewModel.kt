package com.uade.dist.morfando.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN

class LoginViewModel: ViewModel() {
    fun loginSuccess(sharedPreferences: SharedPreferences, id: String){
        /*
            Log.d("account- displayName", this.displayName!!)
            Log.d("account- familyName", this.familyName!!)
            Log.d("account- givenName", this.givenName!!)
            Log.d("account- email", this.email!!)
            Log.d("account- id", this.id!!)
            Log.d("account- id", this.idToken!!) // TODO enviar id a backend y validarlo https://developers.google.com/identity/sign-in/android/backend-auth
            */
        sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN, "1234").apply() //FIXME enviar idToken
    }
}