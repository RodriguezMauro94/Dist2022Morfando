package com.uade.dist.morfando.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.local.SHARED_IS_OWNER
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import com.uade.dist.morfando.domain.AuthenticateUseCase
import kotlinx.coroutines.launch

class OwnerLoginViewModel: ViewModel() {
    private val authenticateUseCase = AuthenticateUseCase()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    var session: SessionModel? = null
    val requestState = MutableLiveData<RequestState>(RequestState.START)

    fun authenticate() {
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            authenticateUseCase.authenticate(
                UserModel(email = email.value!!, password = password.value!!, null)
            ).onSuccess {
                session = it
                requestState.value = RequestState.SUCCESS
            }.onFailure {
                requestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }

    fun completeLogin(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean(SHARED_IS_OWNER, true).apply()
    }
}