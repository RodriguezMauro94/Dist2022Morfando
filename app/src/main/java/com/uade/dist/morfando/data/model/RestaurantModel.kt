package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantModel(@SerializedName("name") val name: String,
                           @SerializedName("cooking_type") val cookingType: String,
                           @SerializedName("price_range") val priceRange: String,
                           @SerializedName("rating") val rating: Long,
                           @SerializedName("neighborhood") val neighborhood: String,
                           @SerializedName("image") val image: String)