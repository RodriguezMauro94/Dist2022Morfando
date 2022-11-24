package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RestaurantModel(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("cookingType") val cookingType: String,
    @SerializedName("priceRange") val priceRange: Int,
    @SerializedName("ranking") val rating: Long,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("image") val image: String,
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("status") var status: String? = null,
    @SerializedName("openHours") val openHours: OpenHoursModel,
    @SerializedName("aboutUs") val aboutUs: String,
    @SerializedName("ratings") val ratings: List<RatingModel>?,
    @SerializedName("images") val images: List<String>?,
    @SerializedName("street") val streetValue: String,
    @SerializedName("number") val streetNumberValue: String,
    @SerializedName("province") val stateValue: String,
    @SerializedName("town") val townValue: String,
    @SerializedName("country") val countryValue: String
): Serializable