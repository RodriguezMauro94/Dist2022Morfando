package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.RestaurantProvider
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    var getRestaurantsUseCase = GetRestaurantsUseCase()
    val restaurantModel = MutableLiveData<RestaurantModel>()

    fun restaurantDetails() {
        restaurantModel.postValue(RestaurantProvider.restaurants.random())
    }

    fun onCreate() {
        viewModelScope.launch {
            val result = getRestaurantsUseCase()
            if(result.isNotEmpty()) {
                restaurantModel.postValue(result.first())
            }
        }
    }
}