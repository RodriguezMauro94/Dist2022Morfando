package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CreateRestaurantModel(
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") val name: String,
    @SerializedName("street") val street: String,
    @SerializedName("streetNumber") val streetNumber: String,
    @SerializedName("state") val state: String,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("town") val town: String,
    @SerializedName("country") val country: String,
    @SerializedName("openHours") val openHours: OpenHoursModel,
    @SerializedName("cookingType") val category: String,
    @SerializedName("priceRange") val priceRange: Int,
    @SerializedName("photos") val photos: List<String>,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double
): Serializable
