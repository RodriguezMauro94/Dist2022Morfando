package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.domain.OwnerChangePasswordDataUseCase
import kotlinx.coroutines.launch

class OwnerForgotPasswordViewModel: ViewModel() {
    private val ownerChangePasswordDataUseCase = OwnerChangePasswordDataUseCase()
    var requestForgotPasswordState = MutableLiveData<RequestState>(RequestState.START)
    var requestValidateOtpState = MutableLiveData<RequestState>(RequestState.START)

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            requestForgotPasswordState.value = RequestState.LOADING
            ownerChangePasswordDataUseCase.forgotPassword(email).onSuccess {
                requestForgotPasswordState.value = RequestState.SUCCESS
            }.onFailure {
                requestForgotPasswordState.value = RequestState.FAILURE(it.toString())
            }
        }
    }

    fun validateOtp(email: String, otpCode: String) {
        viewModelScope.launch {
            requestValidateOtpState.value = RequestState.LOADING
            ownerChangePasswordDataUseCase.validateOtp(email, otpCode).onSuccess {
                requestValidateOtpState.value = RequestState.SUCCESS
            }.onFailure {
                requestValidateOtpState.value = RequestState.FAILURE(it.toString())
            }
        }
    }
}