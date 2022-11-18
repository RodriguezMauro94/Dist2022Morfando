package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel: ViewModel() {
    val restaurantDetails = MutableLiveData<RestaurantDetailsModel?>()
    val restaurant = MutableLiveData<RestaurantModel>()
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    val ratingsList = MutableLiveData<List<RatingModel>>()

    fun getDetails(token: String, code: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
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
                    // TODO dejar esto: requestState.value = RequestState.FAILURE(it.toString())

                    // TODO eliminar:
                    restaurantDetails.value = RestaurantDetailsModel(
                        OpenHoursModel(
                            OpenHoursDayModel("Lunes", "08:00", "10:00", true),
                            OpenHoursDayModel("Martes", "08:00", "10:00", true),
                            OpenHoursDayModel("Miercoles", "08:00", "10:00", true),
                            OpenHoursDayModel("Jueves", "08:00", "10:00", true),
                            OpenHoursDayModel("Viernes", "08:00", "10:00", true),
                            OpenHoursDayModel("Sábado", null, null, false),
                            OpenHoursDayModel("Domingo", "08:00", "12:00", true)
                        ),
                        "Lorem ipsum dolor set amet",
                        listOf(
                            RatingModel("code",  3.0.toLong(), "Copado", "Lorem ipsum dolor set amet", "https://i.imgur.com/GB7lTPH.jpeg"),
                            RatingModel("code",  2.0.toLong(), "Horrible", "Lorem ipsum dolor set amet", "https://i.imgur.com/OK1u0FO.jpeg")
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
                        false,
                        "calle",
                        "123",
                        "Buenos aires",
                        "avellaneda",
                        "argentina"
                    )
                    ratingsList.postValue(restaurantDetails.value!!.ratings!!)
                    requestState.value = RequestState.SUCCESS
                }
        }
    }
}