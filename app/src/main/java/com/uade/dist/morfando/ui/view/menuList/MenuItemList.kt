package com.uade.dist.morfando.ui.view.menuList

abstract class MenuItemList(
    val viewType: MenuViewMode
)

class PlateItemList(
    viewType: MenuViewMode,
    val name: String,
    val price: String,
    val image: String,
    val isVegan: Boolean,
    val isCeliac: Boolean
) : MenuItemList(viewType)

class MenuHeaderItemList(
    viewType: MenuViewMode,
    val title: String
) : MenuItemList(viewType)

enum class MenuViewMode(val viewType: Int) {
    HEADER(0), PLATE(1)
}