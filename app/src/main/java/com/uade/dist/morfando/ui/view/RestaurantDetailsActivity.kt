package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.core.toPriceRange
import com.uade.dist.morfando.data.model.RatingModel
import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SessionModel
import com.uade.dist.morfando.databinding.ActivityRestaurantDetailsBinding
import com.uade.dist.morfando.ui.view.gallery.GalleryActivity
import com.uade.dist.morfando.ui.view.ratingsList.RatingsAdapter
import com.uade.dist.morfando.ui.viewmodel.RestaurantDetailsViewModel

class RestaurantDetailsActivity: AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityRestaurantDetailsBinding
    private val restaurantDetailsViewModel: RestaurantDetailsViewModel by viewModels()
    private var googleMap: GoogleMap? = null
    private lateinit var ratingsAdapter: RatingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setUpMenu()

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

        binding.detailLanding.setOnClickListener {
            restaurantDetailsViewModel.restaurantDetails.value?.let {
                val intent = Intent(this, GalleryActivity::class.java)
                intent.putExtra("details", it)
                startActivity(intent)
            }
        }

        binding.ratingSubmitButton.setOnClickListener {
            val intent = Intent(this, RatingActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivityForResult(intent, RATING_REQUEST_CODE)
        }

        binding.openHoursGroup.setOnClickListener {
            restaurantDetailsViewModel.restaurantDetails.value?.openHours.let {
                val intent = Intent(this, OpenHoursActivity::class.java)
                intent.putExtra("openHours", it)
                startActivity(intent)
            }
        }

        binding.menuGroup.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.places_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ratingsAdapter = RatingsAdapter()
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.restaurantRatingsList.layoutManager = horizontalLayoutManager
        binding.restaurantRatingsList.adapter = ratingsAdapter
        restaurantDetailsViewModel.ratingsList.observe(this) {
            ratingsAdapter.setRatings(it)
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
        binding.restaurantPrice.text = restaurant.priceRange.toPriceRange()
        binding.restaurantType.text = restaurant.cookingType
        binding.restaurantNeighborhood.text = restaurant.neighborhood
        binding.restaurantRating2.rating = restaurant.rating.toFloat()
        binding.restaurantRatingValue2.text = restaurant.rating.toString()
        binding.openHoursTitle.text = getString(R.string.open_hours_title, restaurant.status)
        details.ratings?.let {
            binding.restaurantRatingCount.text =getString(R.string.rating_count, it.size.toString())
            binding.restaurantRatingCount2.text =getString(R.string.rating_count, it.size.toString())
        }
        googleMap?.let {
            val position = LatLng(restaurant.latitude, restaurant.longitude)
            it.addMarker(MarkerOptions().position(position))
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(position, 18f),
                4000,
                null
            )
        }

        binding.openHoursValue.text = details.openHours.getToday().openHours
        binding.aboutUsDescription.text = details.aboutUs
        binding.placesDescription.text = restaurant.neighborhood // FIXME
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map
    }

    private fun setUpMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_fav) {
                    // TODO agregar/eliminar de favoritos y cambiar el icono en consecuencia
                    "Agregar/eliminar a favoritos".showToast(this@RestaurantDetailsActivity)
                }
                return true
            }
        }, this, Lifecycle.State.RESUMED)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RATING_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("rating") as RatingModel
                val ratings: MutableList<RatingModel>? = restaurantDetailsViewModel.restaurantDetails.value?.ratings?.toMutableList()

                ratings?.apply {
                    add(result)
                    ratingsAdapter.setRatings(this)
                }
            }
        }
    }
}