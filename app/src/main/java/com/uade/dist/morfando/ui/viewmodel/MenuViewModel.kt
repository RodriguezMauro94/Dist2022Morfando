package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.ui.view.menuList.MenuItemList
import com.uade.dist.morfando.ui.view.menuList.toViewList
import kotlinx.coroutines.launch
import java.util.*

class MenuViewModel: ViewModel() {
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    val menuList = MutableLiveData<List<MenuItemList>>()

    fun getMenu(token: String, code: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getMenu(code)
                .onSuccess {
                    menuList.postValue(it[0].menu.toViewList())
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    requestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }
}