package com.uade.dist.morfando.data.model

import android.os.Build
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.DayOfWeek
import java.time.LocalDate

data class OpenHoursModel(
    @SerializedName("monday") val monday: OpenHoursDayModel,
    @SerializedName("tuesday") val tuesday: OpenHoursDayModel,
    @SerializedName("wednesday") val wednesday: OpenHoursDayModel,
    @SerializedName("thursday") val thursday: OpenHoursDayModel,
    @SerializedName("friday") val friday: OpenHoursDayModel,
    @SerializedName("saturday") val saturday: OpenHoursDayModel,
    @SerializedName("sunday") val sunday: OpenHoursDayModel,
): Serializable {
    fun getToday(): OpenHoursDayModel {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val today = LocalDate.now()
            return when(today.dayOfWeek) {
                DayOfWeek.MONDAY -> monday
                DayOfWeek.TUESDAY -> tuesday
                DayOfWeek.WEDNESDAY -> wednesday
                DayOfWeek.THURSDAY -> thursday
                DayOfWeek.FRIDAY -> friday
                DayOfWeek.SATURDAY -> saturday
                DayOfWeek.SUNDAY -> sunday
            }
        }
        return monday
    }
}

data class OpenHoursDayModel(
    @SerializedName("day") val day: String,
    @SerializedName("openHours") val openHours: String?,
    @SerializedName("closeHours") val closeHours: String?,
    @SerializedName("isOpen") val isOpen: Boolean,
): Serializable
