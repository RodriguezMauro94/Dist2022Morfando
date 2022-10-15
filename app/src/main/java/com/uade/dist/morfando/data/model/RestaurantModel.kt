package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName

data class RestaurantModel(@SerializedName("name") val name: String,
                           @SerializedName("speciality") val speciality: String,
                           @SerializedName("price") val price: String,
                           @SerializedName("rating") val rating: Long,
                           @SerializedName("neighborhood") val neighborhood: String,
                           @SerializedName("image") val image: String)