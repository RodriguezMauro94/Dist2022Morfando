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
    val requestState = MutableLiveData<RequestState>(RequestState.START)

    fun getPersonalData(sharedPreferences: SharedPreferences, token: String) {
        viewModelScope.launch {
            val userPersonalDataUseCase = UserPersonalDataUseCase(token)
            requestState.value = RequestState.LOADING
            userPersonalDataUseCase.getPersonalData()
                .onSuccess {
                    sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_FAVOURITES, it.favourites.toSet()).apply()
                    requestState.value = RequestState.SUCCESS
                }.onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}