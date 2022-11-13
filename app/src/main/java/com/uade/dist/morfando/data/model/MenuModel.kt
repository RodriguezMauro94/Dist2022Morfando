package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class MenuItemModel(
    @SerializedName("type") val type: String,
    @SerializedName("category") val category: String,
    @SerializedName("plates") val plates: MutableList<PlateModel>
): Serializable

data class PlateModel(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("image") val image: String,
    @SerializedName("isVegan") val isVegan: Boolean,
    @SerializedName("isCeliac") val isCeliac: Boolean
): Serializable
