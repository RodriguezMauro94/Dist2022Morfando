package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.domain.OwnerChangePasswordDataUseCase
import kotlinx.coroutines.launch

class OwnerChangePasswordViewModel: ViewModel() {
    private val ownerChangePasswordDataUseCase = OwnerChangePasswordDataUseCase()
    var requestState = MutableLiveData<RequestState>(RequestState.START)

    fun changePassword(token: String, newPassword: String) {
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            ownerChangePasswordDataUseCase.changePassword(token, newPassword).onSuccess {
                requestState.value = RequestState.SUCCESS
            }.onFailure {
                requestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }
}