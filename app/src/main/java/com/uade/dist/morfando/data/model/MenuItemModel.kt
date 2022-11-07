package com.uade.dist.morfando.data.model

import com.uade.dist.morfando.ui.view.menuList.MenuViewMode

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