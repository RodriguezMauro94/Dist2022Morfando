package com.uade.dist.morfando.ui.view.menuList

import com.uade.dist.morfando.data.model.MenuItemModel
import java.io.Serializable

abstract class MenuItemList

class PlateItemList(
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
    val list = mutableListOf<MenuItemList>()
    this.forEach {
        list.add(MenuHeaderItemList(it.title))
        it.plates.forEach { plate ->
            list.add(
                PlateItemList(
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

    return list
}