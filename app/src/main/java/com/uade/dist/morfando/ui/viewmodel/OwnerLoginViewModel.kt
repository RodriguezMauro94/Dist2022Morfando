package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import com.uade.dist.morfando.domain.AuthenticateUseCase
import kotlinx.coroutines.launch

class OwnerLoginViewModel: ViewModel() {
    val authenticateUseCase = AuthenticateUseCase()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val session = MutableLiveData<SessionModel?>()

    fun authenticate() {
        viewModelScope.launch {
            // TODO show skeleton
            authenticateUseCase(
                UserModel(email = email.value!!, password = password.value!!, null)
            ).let {
                session.postValue(it)
            }
        }
    }
}