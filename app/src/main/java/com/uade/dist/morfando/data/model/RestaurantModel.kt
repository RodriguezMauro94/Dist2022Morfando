package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantModel(@SerializedName("name") val name: String,
                           @SerializedName("cookingType") val cookingType: String,
                           @SerializedName("priceRange") val priceRange: Int,
                           @SerializedName("rating") val rating: Long,
                           @SerializedName("neighborhood") val neighborhood: String,
                           @SerializedName("image") val image: String)