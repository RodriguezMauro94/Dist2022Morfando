package com.uade.dist.morfando.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.*

fun getLocation(context: Context, callback: (address: Address) -> Unit = {}) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
        val location = task.result
        if (location != null) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                callback(address[0])
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}