package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.domain.GetRestaurantsUseCase
import com.uade.dist.morfando.domain.SaveMenuUseCase
import com.uade.dist.morfando.ui.view.menuList.MenuItemList
import com.uade.dist.morfando.ui.view.menuList.toViewList
import kotlinx.coroutines.launch
import java.util.*

class CreateEditMenuViewModel: ViewModel() {
    private val getRestaurantsUseCase = GetRestaurantsUseCase()
    private val saveMenuUseCase = SaveMenuUseCase()
    val getMenuRequestState = MutableLiveData<RequestState>(RequestState.START)
    val saveMenuRequestState = MutableLiveData<RequestState>(RequestState.START)
    val menuViewList = MutableLiveData<List<MenuItemList>>(emptyList())
    val menuLogicList = MutableLiveData<List<MenuItemModel>>(emptyList())

    fun getMenu(code: String) {
        viewModelScope.launch {
            getMenuRequestState.value = RequestState.LOADING
            getRestaurantsUseCase.getMenu(code)
                .onSuccess {
                    menuViewList.postValue(it.menu.toViewList())
                    menuLogicList.postValue(it.menu)
                    getMenuRequestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    // TODO dejar esto: requestState.value = RequestState.FAILURE(it.toString())

                    // TODO eliminar:
                    val list = listOf(
                        MenuItemModel(
                            "Entrada",
                            "Pasta",
                            mutableListOf(
                                PlateModel(
                                    UUID.randomUUID().toString(),
                            "Bastones de muzza",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                            125.0,
                            "https://i.imgur.com/GB7lTPH.jpeg",
                                    isVegan = false,
                                    isCeliac = false
                                ),
                                PlateModel(
                                    UUID.randomUUID().toString(),
                                    "Lorem ipsum",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    3.00,
                                    "https://i.imgur.com/GB7lTPH.jpeg",
                                    isVegan = true,
                                    isCeliac = false
                                ),
                                PlateModel(
                                    UUID.randomUUID().toString(),
                                    "Cosa",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    1.255,
                                    "https://i.imgur.com/GB7lTPH.jpeg",
                                    isVegan = true,
                                    isCeliac = true
                                )
                            )
                        ),
                        MenuItemModel(
                            "Postre",
                            "Carne",
                            mutableListOf(
                                PlateModel(
                                    UUID.randomUUID().toString(),
                                    "Otra cosa",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    125.0,
                                    "https://i.imgur.com/OK1u0FO.jpeg",
                                    isVegan = false,
                                    isCeliac = false
                                ),
                                PlateModel(
                                    UUID.randomUUID().toString(),
                                    "Lorem ipsum",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    3.00,
                                    "https://i.imgur.com/OK1u0FO.jpeg",
                                    isVegan = true,
                                    isCeliac = false
                                ),
                                PlateModel(
                                    UUID.randomUUID().toString(),
                                    "Cosa",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    1.255,
                                    "https://i.imgur.com/OK1u0FO.jpeg",
                                    isVegan = true,
                                    isCeliac = true
                                )
                            )
                        )
                    )
                    menuLogicList.postValue(list)
                    menuViewList.postValue(list.toViewList())
                    getMenuRequestState.value = RequestState.SUCCESS
                }
        }
    }

    fun saveMenu(restaurantCode: String) {
        menuLogicList.value?.let {
            viewModelScope.launch {
                saveMenuRequestState.value = RequestState.LOADING
                saveMenuUseCase.updateMenu(restaurantCode, MenuModel(it))
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