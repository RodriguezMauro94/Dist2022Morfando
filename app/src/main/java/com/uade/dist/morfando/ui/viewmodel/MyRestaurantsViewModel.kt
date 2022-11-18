package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class MyRestaurantsViewModel: ViewModel() {
    val myRestaurants = MutableLiveData<List<RestaurantModel>>()
    val requestState = MutableLiveData<RequestState>(RequestState.START)

    fun getMyRestaurants(token: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurants(SearchFilterOptionsModel()) // FIXME llamar a api correcta
                .onSuccess {
                    myRestaurants.postValue(it)
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}