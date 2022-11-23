package com.uade.dist.morfando.ui.viewmodel.home.home

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.ChipSearchOptionsModel
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_FAVOURITES
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SearchFilterOptionsModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val chipClicked = MutableLiveData<ChipSearchOptionsModel>()
    var latitude: Double? = null
    var longitude: Double? = null
    val myRestaurants = MutableLiveData<List<RestaurantModel>>()
    val myRestaurantsState = MutableLiveData<RequestState>(RequestState.START)
    val nearRestaurants = MutableLiveData<List<RestaurantModel>>()
    val nearRestaurantsState = MutableLiveData<RequestState>(RequestState.START)
    val cheapRestaurants = MutableLiveData<List<RestaurantModel>>()
    val cheapRestaurantsState = MutableLiveData<RequestState>(RequestState.START)
    val trendingRestaurants = MutableLiveData<List<RestaurantModel>>()
    val trendingRestaurantsState = MutableLiveData<RequestState>(RequestState.START)

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

    fun getMyRestaurants(token: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            myRestaurantsState.value = RequestState.LOADING
            getRestaurantsUseCase.getMyRestaurants()
                .onSuccess {
                    myRestaurants.postValue(it)
                    myRestaurantsState.value = RequestState.SUCCESS
                }
                .onFailure {
                    myRestaurantsState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun getNearRestaurants(token: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            val options = SearchFilterOptionsModel().apply {
                distance = 5
            }
            nearRestaurantsState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurants(options)
                .onSuccess {
                    nearRestaurants.postValue(it)
                    nearRestaurantsState.value = RequestState.SUCCESS
                }
                .onFailure {
                    nearRestaurantsState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun getCheapRestaurants(token: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            val options = SearchFilterOptionsModel().apply {
                priceRange = 1
            }
            cheapRestaurantsState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurants(options)
                .onSuccess {
                    cheapRestaurants.postValue(it)
                    cheapRestaurantsState.value = RequestState.SUCCESS
                }
                .onFailure {
                    cheapRestaurantsState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun getTrendingRestaurants(token: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            val options = SearchFilterOptionsModel().apply {
                ratingRange = 5
            }
            trendingRestaurantsState.value = RequestState.LOADING
            getRestaurantsUseCase.getRestaurants(options)
                .onSuccess {
                    trendingRestaurants.postValue(it)
                    trendingRestaurantsState.value = RequestState.SUCCESS
                }
                .onFailure {
                    trendingRestaurantsState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun getPersonalData(sharedPreferences: SharedPreferences, token: String) {
        viewModelScope.launch {
            val userPersonalDataUseCase = UserPersonalDataUseCase(token)
            userPersonalDataUseCase.getPersonalData()
                .onSuccess {
                    val favourites = it.favourites?.toSet() ?: setOf()
                    sharedPreferences.edit().putStringSet(SHARED_PREFERENCES_FAVOURITES, favourites).apply()
                }
        }
    }
}