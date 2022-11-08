package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("username") val name: String?
): Serializable
