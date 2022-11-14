package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.PersonalDataModel
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import kotlinx.coroutines.launch

class PersonalDataViewModel: ViewModel() {
    private val userPersonalDataUseCase = UserPersonalDataUseCase()
    var getRequestState = MutableLiveData<RequestState>(RequestState.START)
    var postRequestState = MutableLiveData<RequestState>(RequestState.START)
    var personalData = MutableLiveData<PersonalDataModel>()

    fun getPersonalData() {
        viewModelScope.launch {
            getRequestState.value = RequestState.LOADING
            userPersonalDataUseCase.getPersonalData().onSuccess {
                personalData.value = it
                getRequestState.value = RequestState.SUCCESS
            }.onFailure {
                getRequestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }

    fun updatePersonalData() {
        personalData.value?.let {
            viewModelScope.launch {
                postRequestState.value = RequestState.LOADING
                userPersonalDataUseCase.updatePersonalData(it).onSuccess {
                    personalData.value = it
                    postRequestState.value = RequestState.SUCCESS
                }.onFailure {
                    postRequestState.value = RequestState.FAILURE(it.toString())
                }
            }
        }
    }
}