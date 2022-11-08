package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.RatingModel
import com.uade.dist.morfando.domain.PublishRatingUseCase
import kotlinx.coroutines.launch

class RatingViewModel: ViewModel() {
    private val publishRatingUseCase = PublishRatingUseCase()
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    var rating: RatingModel? = null

    fun publish(rating: RatingModel) {
        this.rating = rating
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            publishRatingUseCase.publish(rating)
                .onSuccess {
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}