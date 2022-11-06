package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.core.toPriceRange
import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityRestaurantDetailsBinding
import com.uade.dist.morfando.ui.viewmodel.RestaurantDetailsViewModel

class RestaurantDetailsActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRestaurantDetailsBinding
    private val restaurantDetailsViewModel: RestaurantDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val restaurant = intent.extras?.getSerializable("restaurant") as RestaurantModel

        restaurantDetailsViewModel.getDetails(restaurant.code)

        restaurantDetailsViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    restaurantDetailsViewModel.restaurantDetails.value?.apply {
                        completeData(restaurant, this)
                    }
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        binding.ratingSubmitButton.setOnClickListener {
            // TODO
        }
    }

    private fun completeData(restaurant: RestaurantModel, details: RestaurantDetailsModel) {
        Picasso.get()
            .load(restaurant.image)
            .placeholder(R.drawable.logo_morfando)
            .into(binding.detailLanding)
        binding.restaurantName.text = restaurant.name
        binding.restaurantRating.rating = restaurant.rating.toFloat()
        binding.restaurantRatingValue.text = restaurant.rating.toString()
        //TODO binding.restaurantRatingCount.text
        binding.restaurantPrice.text = restaurant.priceRange.toPriceRange()
        binding.restaurantType.text = restaurant.cookingType
        binding.restaurantNeighborhood.text = restaurant.neighborhood
        binding.restaurantRating2.rating = restaurant.rating.toFloat()
        binding.restaurantRatingValue2.text = restaurant.rating.toString()
        binding.openHoursTitle.text = getString(R.string.open_hours_title, restaurant.status)
        //TODO binding.restaurantRatingCount2.text
        //val position = LatLng(restaurant.latitude, restaurant.longitude)
        val position = LatLng(28.043893, -16.539329)
        binding.placesMap.getMapAsync {
            it.addMarker(MarkerOptions().position(position))
            it.moveCamera(CameraUpdateFactory.newLatLng(position))
        }

        binding.openHoursValue.text = details.openHours.getToday().openHours
        binding.aboutUsDescription.text = details.aboutUs
        binding.placesDescription.text = restaurant.neighborhood // FIXME

        //TODO binding.restaurantRatingsList
    }
}