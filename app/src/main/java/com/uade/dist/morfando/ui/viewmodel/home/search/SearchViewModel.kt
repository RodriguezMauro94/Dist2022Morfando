package com.uade.dist.morfando.ui.viewmodel.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val getRestaurantsUseCase = GetRestaurantsUseCase()

    val filteredOptions = MutableLiveData<SearchFilterOptionsModel>().apply {
        value = SearchFilterOptionsModel()
    }

    val searchText = MutableLiveData<String>().apply {
        value = ""
    }

    val searchRestaurants = MutableLiveData<List<RestaurantModel>>()
    fun getRestaurants() {
        viewModelScope.launch {
            // TODO show skeleton
            getRestaurantsUseCase.getRestaurants(filteredOptions.value!!).apply {
                searchRestaurants.postValue(this)
            }
        }
    }
}