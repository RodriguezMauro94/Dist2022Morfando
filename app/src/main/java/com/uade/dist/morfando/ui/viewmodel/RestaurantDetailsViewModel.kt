package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel: ViewModel() {
    val restaurantDetails = MutableLiveData<RestaurantModel?>()
    val restaurant = MutableLiveData<RestaurantModel>()
    lateinit var favourites: MutableSet<String>
    var token = ""
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    val favouritesRequestState = MutableLiveData<RequestState>(RequestState.START)
    val ratingsList = MutableLiveData<List<RatingModel>>()

    fun getDetails(code: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getDetails(code)
                .onSuccess {
                    restaurantDetails.value = it
                    it.ratings?.apply {
                        ratingsList.postValue(this)
                    }
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun addRemoveFavourite() {
        restaurant.value?.apply {
            val useCase = UserPersonalDataUseCase(token)
            viewModelScope.launch {
                favouritesRequestState.value = RequestState.LOADING
                useCase.updateFavourites(favourites)
                    .onSuccess {
                        favouritesRequestState.value = RequestState.SUCCESS
                    }
                    .onFailure {
                        favouritesRequestState.value = RequestState.FAILURE(it.toString())
                    }
            }
        }
    }

    fun getReviews(code: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getReviews(code)
                .onSuccess {
                    ratingsList.postValue(it)
                }
                .onFailure {
                    ratingsList.postValue(listOf())
                }
        }
    }
}