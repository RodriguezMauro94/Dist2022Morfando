package com.uade.dist.morfando.ui.viewmodel.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    val searchRestaurants = MutableLiveData<List<RestaurantModel>>()
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    val searchText = MutableLiveData("")

    val filteredOptions = MutableLiveData<SearchFilterOptionsModel>().apply {
        value = SearchFilterOptionsModel()
    }

    fun getRestaurants(token: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurants(filteredOptions.value!!)
                .onSuccess {
                    searchRestaurants.postValue(it)
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}