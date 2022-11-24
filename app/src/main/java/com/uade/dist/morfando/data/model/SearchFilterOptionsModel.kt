package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchFilterOptionsModel(
    @SerializedName("openNow") var openNow: Boolean = true,
    @SerializedName("priceRange") var priceRange: Int? = null,
    @SerializedName("ratingRange") var ratingRange: Int? = null,
    @SerializedName("cookingType") var cookingType: String? = null,
    @SerializedName("distance") var distance: Int = 5,
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null,
    @SerializedName("name") var name: String? = "",
): Serializable