package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SaveMenuModel(
    @SerializedName("code") val restaurantCode: String,
    @SerializedName("menu") val menu: List<MenuItemModel>
): Serializable
