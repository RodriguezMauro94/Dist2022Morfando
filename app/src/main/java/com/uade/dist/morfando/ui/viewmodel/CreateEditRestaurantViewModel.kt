package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.CreateRestaurantModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.domain.CreateRestaurantUseCase
import kotlinx.coroutines.launch

class CreateEditRestaurantViewModel: ViewModel() {
    private val createRestaurantUseCase = CreateRestaurantUseCase()
    val originalRestaurant = MutableLiveData<RestaurantModel?>()
    val createRequestState = MutableLiveData<RequestState>(RequestState.START)

    fun createRestaurant(createRestaurantModel: CreateRestaurantModel) {
        viewModelScope.launch {
            createRequestState.value = RequestState.LOADING
            createRestaurantUseCase.createRestaurant(createRestaurantModel)
                .onSuccess {
                    //TODO
                    createRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    createRequestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}