package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import kotlinx.coroutines.launch

class MyProfileViewModel: ViewModel() {
    val deleteAccountRequestState = MutableLiveData<RequestState>(RequestState.START)

    fun deleteAccount(token: String) {
        val userPersonalDataUseCase = UserPersonalDataUseCase(token)
        viewModelScope.launch {
            deleteAccountRequestState.value = RequestState.LOADING
            userPersonalDataUseCase.deleteAccount()
            .onSuccess {
                deleteAccountRequestState.value = RequestState.SUCCESS
            }.onFailure {
                deleteAccountRequestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }
}