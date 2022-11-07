package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MenuItemModel(
    @SerializedName("title") val title: String,
    @SerializedName("plates") val plates: List<PlateModel>
): Serializable

data class PlateModel(
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val image: String,
    @SerializedName("isVegan") val isVegan: Boolean,
    @SerializedName("isCeliac") val isCeliac: Boolean
): Serializable
