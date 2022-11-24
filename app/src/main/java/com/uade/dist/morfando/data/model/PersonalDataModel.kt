package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PersonalDataModel(
    @SerializedName("name") var name: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("icon") var image: String,
    @SerializedName("favorites") val favourites: List<String>?
): Serializable

data class FavoritesUpdateModel(
    @SerializedName("favorites") val favourites: List<String>
)
