package com.uade.dist.morfando.ui.viewmodel.home.favourites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class FavouritesViewModel : ViewModel() {
    val getRestaurantsUseCase = GetRestaurantsUseCase()

    val favouritesRestaurants = MutableLiveData<List<RestaurantModel>>()
    fun getRestaurants() {
        viewModelScope.launch {
            // TODO show skeleton
            getRestaurantsUseCase.getAllRestaurants().apply {
                favouritesRestaurants.postValue(this)
            }
        }
    }
}
