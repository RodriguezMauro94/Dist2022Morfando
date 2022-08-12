package com.uade.dist.morfando.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.uade.dist.morfando.model.RestaurantModel
import com.uade.dist.morfando.model.RestaurantProvider

class MainViewModel: ViewModel() {
    val restaurantModel = MutableLiveData<RestaurantModel>()

    fun restaurantDetails() {
        restaurantModel.postValue(RestaurantProvider.random())
    }
}