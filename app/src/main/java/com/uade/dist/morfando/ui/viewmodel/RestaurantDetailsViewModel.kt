package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel: ViewModel() {
    private val getRestaurantsUseCase = GetRestaurantsUseCase()
    val restaurantDetails = MutableLiveData<RestaurantDetailsModel?>()
    val restaurant = MutableLiveData<RestaurantModel>()
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    val ratingsList = MutableLiveData<List<RatingModel>>()

    fun getDetails(code: String) {
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurantDetail(code)
                .onSuccess {
                    restaurantDetails.value = it
                    it.ratings?.apply {
                        ratingsList.postValue(this)
                    }
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    //TODO dejar esto: requestState.value = RequestState.FAILURE(it.toString())

                    // TODO eliminar:
                    restaurantDetails.value = RestaurantDetailsModel(
                        OpenHoursModel(
                            OpenHoursDayModel("Lunes", "08 hs", "10 hs", true),
                            OpenHoursDayModel("Martes", "08 hs", "10 hs", true),
                            OpenHoursDayModel("Miercoles", "08 hs", "10 hs", true),
                            OpenHoursDayModel("Jueves", "08 hs", "10 hs", true),
                            OpenHoursDayModel("Viernes", "08 hs", "10 hs", true),
                            OpenHoursDayModel("Sábado", null, null, false),
                            OpenHoursDayModel("Domingo", "08 hs", "12 hs", true)
                        ),
                        "Lorem ipsum dolor set amet",
                        listOf(
                            RatingModel("jorge", "code",  3.0.toLong(), "Copado", "Lorem ipsum dolor set amet", "https://i.imgur.com/GB7lTPH.jpeg"),
                            RatingModel("Ricardo", "code",  2.0.toLong(), "Horrible", "Lorem ipsum dolor set amet", "https://i.imgur.com/OK1u0FO.jpeg")
                        ),
                        listOf(
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                            "https://i.imgur.com/GB7lTPH.jpeg",
                        ),
                        false
                    )
                    ratingsList.postValue(restaurantDetails.value!!.ratings!!)
                    requestState.value = RequestState.SUCCESS
                }
        }
    }
}