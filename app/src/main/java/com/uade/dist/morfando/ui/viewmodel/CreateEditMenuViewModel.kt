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

class CreateEditMenuViewModel: ViewModel() {
    private val getRestaurantsUseCase = GetRestaurantsUseCase()
    val requestState = MutableLiveData<RequestState>(RequestState.START)
    val menuList = MutableLiveData<List<MenuItemList>>()

    fun getMenu(code: String) {
        viewModelScope.launch {
            requestState.value = RequestState.LOADING
            getRestaurantsUseCase.getMenu(code)
                .onSuccess {
                    menuList.postValue(it.toViewList())
                    requestState.value = RequestState.SUCCESS
                }
                .onFailure {
                    //TODO dejar esto: requestState.value = RequestState.FAILURE(it.toString())

                    // TODO eliminar:
                    val list = listOf(
                        MenuItemModel(
                            "Entrada",
                            listOf(
                                PlateModel(
                            "Bastones de muzza",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                            125.0,
                            "https://i.imgur.com/GB7lTPH.jpeg",
                                    isVegan = false,
                                    isCeliac = false
                                ),
                                PlateModel(
                                    "Lorem ipsum",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    3.00,
                                    "https://i.imgur.com/GB7lTPH.jpeg",
                                    isVegan = true,
                                    isCeliac = false
                                ),
                                PlateModel(
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
                            "Platos principales",
                            listOf(
                                PlateModel(
                                    "Otra cosa",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    125.0,
                                    "https://i.imgur.com/OK1u0FO.jpeg",
                                    isVegan = false,
                                    isCeliac = false
                                ),
                                PlateModel(
                                    "Lorem ipsum",
                                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam turpis neque, dignissim ac fringilla eu, tincidunt et nisl. Vivamus hendrerit tellus vel varius scelerisque.",
                                    3.00,
                                    "https://i.imgur.com/OK1u0FO.jpeg",
                                    isVegan = true,
                                    isCeliac = false
                                ),
                                PlateModel(
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
                    menuList.postValue(list.toViewList())
                    requestState.value = RequestState.SUCCESS
                }
        }
    }
}