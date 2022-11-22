package com.uade.dist.morfando.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_FAVOURITES
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    lateinit var userPersonalDataUseCase: UserPersonalDataUseCase
    var getRequestState = MutableLiveData<RequestState>(RequestState.START)

    fun getPersonalData(sharedPreferences: SharedPreferences) {
        viewModelScope.launch {
            getRequestState.value = RequestState.LOADING
            userPersonalDataUseCase.getPersonalData().onSuccess {
                sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_FAVOURITES, it.favourites.toSet()).apply()
                getRequestState.value = RequestState.SUCCESS
            }.onFailure {
                getRequestState.value = RequestState.FAILURE(it.toString())
            }
        }
    }
}