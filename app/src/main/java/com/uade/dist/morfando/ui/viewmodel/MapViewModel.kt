package com.uade.dist.morfando.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapViewModel: ViewModel() {
    val marker = MutableLiveData<MarkerOptions>()
    val coordinatesZoom = MutableLiveData<LatLng>()

    fun onMapReady() {
        val coordinates = LatLng(28.043893, -16.539329)
        coordinatesZoom.postValue(coordinates)
        marker.postValue(MarkerOptions().position(coordinates).title("Tenerife"))
    }
}