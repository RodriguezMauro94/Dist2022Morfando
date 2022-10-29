package com.uade.dist.morfando.ui.viewmodel.home.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.ui.view.home.search.SearchFilterOptions
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    val getRestaurantsUseCase = GetRestaurantsUseCase()

    val filteredOptions = MutableLiveData<SearchFilterOptions>().apply {
        value = SearchFilterOptions()
    }

    val searchText = MutableLiveData<String>().apply {
        value = ""
    }

    val searchRestaurants = MutableLiveData<List<RestaurantModel>>()
    fun getRestaurants() {
        viewModelScope.launch {
            // TODO show skeleton
            getRestaurantsUseCase().apply {
                searchRestaurants.postValue(this)
            }
        }
    }
}