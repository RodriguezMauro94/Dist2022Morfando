package com.uade.dist.morfando.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SessionModel(
    @SerializedName("sessionEnc") val session: String
): Serializable