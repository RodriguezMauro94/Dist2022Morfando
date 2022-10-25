package com.uade.dist.morfando.ui.viewmodel.home.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val getRestaurantsUseCase = GetRestaurantsUseCase()
    val chipClicked = MutableLiveData<Int>()

    fun chipTapped(chip: String) {
        chipClicked.value = _chips.value?.get(chip)
    }

    private val _chips = MutableLiveData<Map<String, Int>>().apply {
        value = mapOf(
            Pair("trending", R.string.chip_trending),
            Pair("classic", R.string.chip_classic),
            Pair("near", R.string.chip_near),
            Pair("cheap", R.string.chip_cheap),
            Pair("expensive", R.string.chip_expensive)
        )
    }
    val chips: LiveData<Map<String, Int>> = _chips

    val nearRestaurants = MutableLiveData<List<RestaurantModel>>()
    fun getNearRestaurants() {
        viewModelScope.launch {
            // TODO show skeleton
            getRestaurantsUseCase().apply {
                nearRestaurants.value = this
            }
        }
    }
}