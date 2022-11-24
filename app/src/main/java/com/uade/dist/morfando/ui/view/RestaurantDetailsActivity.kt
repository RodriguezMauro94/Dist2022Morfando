package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_FAVOURITES
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.RatingModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityRestaurantDetailsBinding
import com.uade.dist.morfando.ui.view.home.categories.categories
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

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""
        val favourites = sharedPreferences.getStringSet(SHARED_PREFERENCES_FAVOURITES, null) ?: setOf()

        val restaurant = intent.extras?.getSerializable("restaurant") as RestaurantModel

        restaurantDetailsViewModel.favourites = favourites.toMutableSet()
        restaurantDetailsViewModel.token = token
        restaurantDetailsViewModel.restaurant.postValue(restaurant)

        binding.loading.visibility = View.GONE
        binding.detailsMain.visibility = View.VISIBLE
        completeData(restaurant)

        restaurantDetailsViewModel.getDetails(restaurant.code)
        restaurantDetailsViewModel.getReviews(restaurant.code)

        restaurantDetailsViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.detailsMain.visibility = View.GONE
                }
                is RequestState.SUCCESS -> {
                    binding.loading.visibility = View.GONE
                    binding.detailsMain.visibility = View.VISIBLE
                    restaurantDetailsViewModel.restaurantDetails.value?.apply {
                        completeData(this)
                    }
                }
                is RequestState.FAILURE -> {
                    binding.loading.visibility = View.GONE
                    binding.detailsMain.visibility = View.GONE
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        binding.detailLanding.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivity(intent)
        }

        binding.ratingSubmitButton.setOnClickListener {
            val intent = Intent(this, RatingActivity::class.java)
            intent.putExtra("restaurant", restaurant)
            startActivityForResult(intent, RATING_REQUEST_CODE)
        }

        binding.openHoursGroup.setOnClickListener {
            val intent = Intent(this, OpenHoursActivity::class.java)
            intent.putExtra("openHours", restaurant.openHours)
            startActivity(intent)
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

    private fun completeData(restaurant: RestaurantModel) {
        val category = categories.find { category ->
            category.id == restaurant.cookingType
        }

        restaurant.images?.get(0)?.apply {
            Picasso.get()
                .load(this)
                .placeholder(R.drawable.logo_morfando)
                .into(binding.detailLanding)
        }

        binding.restaurantName.text = restaurant.name
        binding.restaurantRating.rating = restaurant.rating.toFloat()
        binding.restaurantRatingValue.text = restaurant.rating.toString()
        binding.restaurantPrice.text = restaurant.priceRange.toPriceRange()
        binding.restaurantType.text = getString(category!!.text)
        binding.restaurantNeighborhood.text = restaurant.neighborhood
        binding.restaurantRating2.rating = restaurant.rating.toFloat()
        binding.restaurantRatingValue2.text = restaurant.rating.toString()
        binding.openHoursTitle.text = getString(R.string.open_hours_title, restaurant.status)
        restaurant.ratings?.let {
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

        binding.openHoursValue.text = if (restaurant.openHours.getToday().isOpen)
                getString(R.string.open_hours_template, restaurant.openHours.getToday().openHours, restaurant.openHours.getToday().closeHours)
            else
                getString(R.string.open_hours_closed)
        binding.aboutUsDescription.text = restaurant.aboutUs
        binding.placesDescription.text = "${restaurant.streetValue} ${restaurant.streetNumberValue}, ${restaurant.neighborhood}, ${restaurant.townValue}, ${restaurant.stateValue}, ${restaurant.countryValue}"
    }

    override fun onMapReady(map: GoogleMap) {
        this.googleMap = map

        googleMap?.setOnMapClickListener {
            val restaurant = restaurantDetailsViewModel.restaurant.value
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/?api=1&query=${restaurant?.latitude}%2C${restaurant?.longitude}")
            )
            startActivity(intent)
        }
    }

    private fun setUpMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.details_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val menuItem = menu.findItem(R.id.action_fav)
                menuItem?.let {
                    updateFavouritesIcon(menuItem)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.action_fav) {
                    val favourites = restaurantDetailsViewModel.favourites
                    val code = restaurantDetailsViewModel.restaurant.value!!.code
                    if (favourites.contains(code)) {
                        favourites.remove(code)
                    } else {
                        favourites.add(code)
                    }
                    updateFavouritesIcon(menuItem)

                    restaurantDetailsViewModel.addRemoveFavourite()
                }
                return true
            }
        }, this, Lifecycle.State.RESUMED)
    }

    private fun updateFavouritesIcon(menuItem: MenuItem) {
        val favourites = restaurantDetailsViewModel.favourites
        val code = restaurantDetailsViewModel.restaurant.value!!.code
        if (favourites.contains(code)) {
            menuItem.icon = ContextCompat.getDrawable(
                this@RestaurantDetailsActivity,
                R.drawable.ic_favorites_24
            )
        } else {
            menuItem.icon = ContextCompat.getDrawable(
                this@RestaurantDetailsActivity,
                R.drawable.ic_favorite_border_24
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RATING_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("rating") as RatingModel
                val ratings: MutableList<RatingModel>? = restaurantDetailsViewModel.restaurant.value?.ratings?.toMutableList()

                ratings?.apply {
                    add(result)
                    ratingsAdapter.setRatings(this)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}