package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.MenuItemModel
import com.uade.dist.morfando.data.model.MenuModel
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.domain.SaveMenuUseCase
import com.uade.dist.morfando.ui.view.menuList.MenuItemList
import com.uade.dist.morfando.ui.view.menuList.toViewList
import kotlinx.coroutines.launch

class CreateEditMenuViewModel: ViewModel() {
    val getMenuRequestState = MutableLiveData<RequestState>(RequestState.START)
    val saveMenuRequestState = MutableLiveData<RequestState>(RequestState.START)
    val menuViewList = MutableLiveData<List<MenuItemList>>(emptyList())
    val menuLogicList = MutableLiveData<List<MenuItemModel>>(emptyList())
    var token = ""

    fun getMenu(code: String) {
        val getRestaurantsUseCase = GetRestaurantsUseCase(token)
        viewModelScope.launch {
            getMenuRequestState.value = RequestState.LOADING
            getRestaurantsUseCase.getMenu(code)
                .onSuccess {
                    menuViewList.postValue(it[0].menu.toViewList())
                    menuLogicList.postValue(it[0].menu)
                    getMenuRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    getMenuRequestState.value = RequestState.FAILURE(it.toString())
                }
        }
    }

    fun saveMenu(restaurantCode: String) {
        val saveMenuUseCase = SaveMenuUseCase(token, restaurantCode)
        menuLogicList.value?.let {
            viewModelScope.launch {
                saveMenuRequestState.value = RequestState.LOADING
                saveMenuUseCase.updateMenu(MenuModel(it))
                .onSuccess {
                    saveMenuRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    saveMenuRequestState.value = RequestState.FAILURE(it.toString())
                }
            }
        }
    }
}