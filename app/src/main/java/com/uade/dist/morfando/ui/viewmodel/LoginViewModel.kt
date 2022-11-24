package com.uade.dist.morfando.ui.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.UserModel
import com.uade.dist.morfando.domain.AuthenticateUseCase
import com.uade.dist.morfando.domain.RegisterUseCase
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val registerUseCase = RegisterUseCase()
    private val authUseCase = AuthenticateUseCase()
    var requestState = MutableLiveData<RequestState>(RequestState.START)
    var id = ""

    fun loginSuccess(sharedPreferences: SharedPreferences, id: String){
        sharedPreferences.edit().putString(SHARED_PREFERENCES_TOKEN, id).apply()
    }

    fun googleLoginSuccess(account: GoogleSignInAccount) {
        viewModelScope.launch {
            val user = UserModel(account.email!!, account.id!!, account.displayName!!)
            requestState.value = RequestState.LOADING
            registerUseCase.register(
                user
            ).onSuccess {
                authUseCase.authenticate(UserModel(account.email!!, account.id!!, null)).onSuccess {
                    id = it.session
                    requestState.value = RequestState.SUCCESS
                }.onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
            }.onFailure {
                authUseCase.authenticate(UserModel(account.email!!, account.id!!, null)).onSuccess {
                    id = it.session
                    requestState.value = RequestState.SUCCESS
                }.onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
            }
        }
    }
}