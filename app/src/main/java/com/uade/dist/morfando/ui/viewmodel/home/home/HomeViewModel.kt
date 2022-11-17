package com.uade.dist.morfando.ui.viewmodel.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.ChipSearchOptionsModel
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val getRestaurantsUseCase = GetRestaurantsUseCase()
    val chipClicked = MutableLiveData<ChipSearchOptionsModel>()

    fun chipTapped(chip: String) {
        chipClicked.postValue(_chips.value?.get(chip))
    }

    private val _chips = MutableLiveData<Map<String, ChipSearchOptionsModel>>().apply {
        value = mapOf(
            Pair("trending", ChipSearchOptionsModel(R.string.chip_trending, SearchFilterOptionsModel(ratingRange = 3))),
            Pair("classic", ChipSearchOptionsModel(R.string.chip_classic, SearchFilterOptionsModel(ratingRange = 4))),
            Pair("near", ChipSearchOptionsModel(R.string.chip_near, SearchFilterOptionsModel(distance = 5))),
            Pair("cheap", ChipSearchOptionsModel(R.string.chip_cheap, SearchFilterOptionsModel(priceRange = 1))),
            Pair("expensive", ChipSearchOptionsModel(R.string.chip_expensive, SearchFilterOptionsModel(priceRange = 4)))
        )
    }
    val chips: LiveData<Map<String, ChipSearchOptionsModel>> = _chips

    val nearRestaurants = MutableLiveData<List<RestaurantModel>>()
    val nearRestaurantsState = MutableLiveData<RequestState>(RequestState.START)
    fun getNearRestaurants() {
        viewModelScope.launch {
            nearRestaurantsState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurants(SearchFilterOptionsModel())
                .onSuccess {
                    nearRestaurants.postValue(it)
                    nearRestaurantsState.value = RequestState.SUCCESS
                }
                .onFailure {
                    nearRestaurantsState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}