package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.CreateRestaurantUseCase
import com.uade.dist.morfando.domain.EditRestaurantUseCase
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.domain.SaveMenuUseCase
import kotlinx.coroutines.launch

class CreateEditRestaurantViewModel: ViewModel() {
    private val createRestaurantUseCase = CreateRestaurantUseCase()
    private val editRestaurantUseCase = EditRestaurantUseCase()
    val originalRestaurant = MutableLiveData<RestaurantModel?>()
    val restaurantDetails = MutableLiveData<RestaurantDetailsModel?>()
    val createRequestState = MutableLiveData<RequestState>(RequestState.START)
    val editRequestState = MutableLiveData<RequestState>(RequestState.START)
    val deleteRequestState = MutableLiveData<RequestState>(RequestState.START)
    val updateStatusRequestState = MutableLiveData<RequestState>(RequestState.START)
    val detailsRequestState = MutableLiveData<RequestState>(RequestState.START)
    val menu = MutableLiveData<MenuModel?>()
    var token = ""

    fun getRestaurantDetails(code: String) {
        val restaurantUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            detailsRequestState.value = RequestState.LOADING
            restaurantUseCase.getRestaurantDetail(code)
                .onSuccess {
                    restaurantDetails.postValue(it)
                    detailsRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    detailsRequestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun createRestaurant(createRestaurantModel: CreateRestaurantModel) {
        viewModelScope.launch {
            createRequestState.value = RequestState.LOADING
            createRestaurantUseCase.createRestaurant(token, createRestaurantModel)
                .onSuccess {
                    SaveMenuUseCase(token, it.code).saveMenu(menu.value!!)
                        .onSuccess {
                            createRequestState.value = RequestState.SUCCESS
                        }
                        .onFailure {
                            createRequestState.value = RequestState.FAILURE(it.toString())
                        }
                }
                .onFailure {
                    createRequestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun editRestaurant(createRestaurantModel: CreateRestaurantModel) {
        viewModelScope.launch {
            editRequestState.value = RequestState.LOADING
            editRestaurantUseCase.editRestaurant(token, createRestaurantModel)
                .onSuccess {
                    editRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    editRequestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun deleteRestaurant() {
        viewModelScope.launch {
            originalRestaurant.value?.let {
                deleteRequestState.value = RequestState.LOADING
                editRestaurantUseCase.deleteRestaurant(token, it.code)
                    .onSuccess {
                        deleteRequestState.value = RequestState.SUCCESS
                    }
                    .onFailure {
                        deleteRequestState.value = RequestState.FAILURE(it.toString())
                    }
            }
        }
    }

    fun updateStatus(state: String) {
        viewModelScope.launch {
            originalRestaurant.value?.let {
                updateStatusRequestState.value = RequestState.LOADING
                editRestaurantUseCase.updateStatus(token, it.code, state)
                    .onSuccess { result ->
                        originalRestaurant.value!!.status = result.status
                        updateStatusRequestState.value = RequestState.SUCCESS
                    }
                    .onFailure {
                        updateStatusRequestState.value = RequestState.FAILURE(it.toString())
                    }
            }
        }
    }
}