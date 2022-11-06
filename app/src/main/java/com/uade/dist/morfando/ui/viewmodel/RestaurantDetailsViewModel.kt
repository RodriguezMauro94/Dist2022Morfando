package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.OpenHoursDayModel
import com.uade.dist.morfando.data.model.OpenHoursModel
import com.uade.dist.morfando.data.model.RatingModel
import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class RestaurantDetailsViewModel: ViewModel() {
    private val getRestaurantsUseCase = GetRestaurantsUseCase()
    val restaurantDetails = MutableLiveData<RestaurantDetailsModel?>()
    val requestState = MutableLiveData<RequestState>(RequestState.START)

    fun getDetails(code: String) {
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurantDetail(code)
                .onSuccess {
                    restaurantDetails.value = it
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    //TODO dejar esto: requestState.value = RequestState.FAILURE(it.toString())

                    // TODO eliminar:
                    restaurantDetails.value = RestaurantDetailsModel(
                        OpenHoursModel(
                            OpenHoursDayModel("Lunes", "08-10 hs"),
                            OpenHoursDayModel("Martes", "08-10 hs"),
                            OpenHoursDayModel("Miercoles", "08-10 hs"),
                            OpenHoursDayModel("Jueves", "08-10 hs"),
                            OpenHoursDayModel("Viernes", "08-10 hs"),
                            OpenHoursDayModel("Sábado", "08-10 hs"),
                            OpenHoursDayModel("Domingo", "08-12 hs")
                        ),
                        "Lorem ipsum dolor set amet",
                        listOf(
                            RatingModel("jorge", 3.0.toLong(), "Copado", "Lorem ipsum dolor set amet"),
                            RatingModel("Ricardo", 2.0.toLong(), "Horrible", "Lorem ipsum dolor set amet")
                        ),
                        listOf(
                            "https://i.imgur.com/GB7lTPH.jpeg"
                        )
                    )
                    requestState.value = RequestState.SUCCESS
                }
        }
    }
}