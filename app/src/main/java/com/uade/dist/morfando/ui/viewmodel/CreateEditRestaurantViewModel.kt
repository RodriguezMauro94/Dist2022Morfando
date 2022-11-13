package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.CreateRestaurantUseCase
import com.uade.dist.morfando.domain.EditRestaurantUseCase
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class CreateEditRestaurantViewModel: ViewModel() {
    private val createRestaurantUseCase = CreateRestaurantUseCase()
    private val editRestaurantUseCase = EditRestaurantUseCase()
    private val restaurantUseCase = GetRestaurantsUseCase()
    val originalRestaurant = MutableLiveData<RestaurantModel?>()
    val restaurantDetails = MutableLiveData<RestaurantDetailsModel?>()
    val createRequestState = MutableLiveData<RequestState>(RequestState.START)
    val editRequestState = MutableLiveData<RequestState>(RequestState.START)
    val detailsRequestState = MutableLiveData<RequestState>(RequestState.START)

    fun getRestaurantDetails(code: String) {
        viewModelScope.launch {
            detailsRequestState.value = RequestState.LOADING
            restaurantUseCase.getRestaurantDetail(code)
                .onSuccess {
                    restaurantDetails.postValue(it)
                    detailsRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    //FIXME dejar esto: detailsRequestState.value = RequestState.FAILURE(it.toString())

                    restaurantDetails.value = RestaurantDetailsModel(
                        OpenHoursModel(
                            OpenHoursDayModel("Lunes", null, null, false),
                            OpenHoursDayModel("Martes", "08:00", "10:00", true),
                            OpenHoursDayModel("Miercoles", "08:00", "10:00", true),
                            OpenHoursDayModel("Jueves", "08:00", "10:00", true),
                            OpenHoursDayModel("Viernes", "08:00", "10:00", true),
                            OpenHoursDayModel("Sábado", null, null, false),
                            OpenHoursDayModel("Domingo", "08:00", "12:00", true)
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
                        false,
                        "calle",
                        "123",
                        "Buenos aires",
                        "avellaneda",
                        "argentina"
                    )
                    detailsRequestState.value = RequestState.SUCCESS
                }
        }
    }

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

    fun editRestaurant(createRestaurantModel: CreateRestaurantModel) {
        viewModelScope.launch {
            editRequestState.value = RequestState.LOADING
            editRestaurantUseCase.editRestaurant(createRestaurantModel)
                .onSuccess {
                    //TODO
                    editRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    editRequestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}