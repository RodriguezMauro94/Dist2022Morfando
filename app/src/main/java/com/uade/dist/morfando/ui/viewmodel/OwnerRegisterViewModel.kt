package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import com.uade.dist.morfando.domain.AuthenticateUseCase
import com.uade.dist.morfando.domain.RegisterUseCase
import kotlinx.coroutines.launch

class OwnerRegisterViewModel: ViewModel() {
    private val registerUseCase = RegisterUseCase()
    private val authUseCase = AuthenticateUseCase()
    var session: SessionModel? = null
    var requestState = MutableLiveData<RequestState>(RequestState.START)

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            val user = UserModel(email, password, name)
            requestState.value = RequestState.LOADING
            registerUseCase.register(
                user
            ).onSuccess {
                authUseCase.authenticate(UserModel(email, password, null)).onSuccess {
                    session = it
                    requestState.value = RequestState.SUCCESS
                }.onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
            }.onFailure {
                requestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }
}