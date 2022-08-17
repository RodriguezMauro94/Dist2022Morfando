package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.uade.dist.morfando.R
import com.uade.dist.morfando.ui.viewmodel.MapViewModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val mapViewModel: MapViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        mapViewModel.onMapReady()

        mapViewModel.marker.observe(this) {
            map.addMarker(it)
        }

        mapViewModel.coordinatesZoom.observe(this) {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(it, 18f),
                4000,
                null
            )
        }
    }
}