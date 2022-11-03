package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SearchFilterOptionsModel(
    @SerializedName("openNow") var openNow: Boolean = false,
    @SerializedName("priceRange") var priceRange: Int? = null,
    @SerializedName("ratingRange") var ratingRange: Int? = null,
    @SerializedName("cookingType") var cookingType: String? = null,
    @SerializedName("distance") var distance: Int = 5
): Serializable