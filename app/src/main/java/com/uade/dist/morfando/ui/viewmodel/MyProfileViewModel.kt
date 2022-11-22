package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.data.model.UserModel
import com.uade.dist.morfando.domain.AuthenticateUseCase
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import kotlinx.coroutines.launch

class MyProfileViewModel: ViewModel() {
    val requestState = MutableLiveData<RequestState>(RequestState.START)

    fun deleteAccount(token: String) {
        val userPersonalDataUseCase = UserPersonalDataUseCase(token)
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            userPersonalDataUseCase.deleteAccount()
            .onSuccess {
                requestState.value = RequestState.SUCCESS
            }.onFailure {
                requestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }
}