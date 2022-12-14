package com.uade.dist.morfando.ui.view

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.getLocation
import com.uade.dist.morfando.databinding.ActivityRequestGeoPermissionBinding
import com.uade.dist.morfando.ui.view.home.HomeActivity

class RequestGeoPermissionActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRequestGeoPermissionBinding
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityRequestGeoPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val permissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, permissions) || permissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION, permissions) -> {
                    checkGPS()
                }
                else -> {
                    Toast.makeText(this, "permiso denegado", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.geoRequest.setOnClickListener {
            checkLocationPermission(permissionRequest)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkGPS()
        }
    }

    private fun permissionGranted(permission: String, permissions: Map<String, @JvmSuppressWildcards Boolean>)
        = permissions.containsKey(permission) && permissions.getValue(permission)

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun checkLocationPermission(permissionRequest: ActivityResultLauncher<Array<String>>) {
        // FIXME falta chequear si clav?? "No volver a preguntar"
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // permission already granted
            checkGPS()
        } else {
            // permission not granted
            permissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun checkGPS() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 2000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this.applicationContext)
            .checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                // GPS is on
                val response = task.getResult(ApiException::class.java)
                getLocation(this) {
                    goToHome()
                }
            } catch (e: ApiException) {
                // GPS is off
                e.printStackTrace()
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        // request for enable GPS
                        val resolveApiException = e as ResolvableApiException
                        resolveApiException.startResolutionForResult(this, 200)
                    } catch (sendIntentException: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // when the setting is unavailable
                    }
                }
            }
        }
    }
}