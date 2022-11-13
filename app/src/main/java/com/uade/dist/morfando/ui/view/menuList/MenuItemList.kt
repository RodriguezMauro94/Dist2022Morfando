package com.uade.dist.morfando.ui.view.menuList

import com.uade.dist.morfando.data.model.MenuItemModel
import com.uade.dist.morfando.data.model.PlateModel
import java.io.Serializable

abstract class MenuItemList

class PlateItemList(
    val code: String,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val isVegan: Boolean,
    val isCeliac: Boolean
) : MenuItemList(), Serializable

class MenuHeaderItemList(
    val title: String
) : MenuItemList()

enum class MenuViewMode(val viewType: Int) {
    HEADER(0), PLATE(1)
}

fun List<MenuItemModel>.toViewList(): List<MenuItemList> {
    val sortedList = this.sortedBy { it.type }

    val mapSortByTypeAndCategory = mutableMapOf<String, List<PlateModel>>()
    sortedList.forEach {
        val key = "${it.type} - ${it.category}"
        mapSortByTypeAndCategory[key] = it.plates
    }

    val list = mutableListOf<MenuItemList>()
    mapSortByTypeAndCategory.asIterable().forEach {
        if (it.value.isNotEmpty()) {
            list.add(MenuHeaderItemList(it.key))
            it.value.forEach { plate ->
                list.add(
                    PlateItemList(
                        plate.code,
                        plate.name,
                        plate.description,
                        plate.price,
                        plate.image,
                        plate.isVegan,
                        plate.isCeliac
                    )
                )
            }
        }
    }

    return list
}