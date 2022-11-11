package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityMyRestaurantsBinding
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantViewMode
import com.uade.dist.morfando.ui.view.restaurantList.RestaurantsAdapter
import com.uade.dist.morfando.ui.viewmodel.MyRestaurantsViewModel

const val ADD_RESTAURANT_REQUEST_CODE = 4000

class MyRestaurantsActivity: AppCompatActivity(), RestaurantsAdapter.ItemClickListener {
    private lateinit var binding: ActivityMyRestaurantsBinding
    private lateinit var myRestaurantsAdapter: RestaurantsAdapter
    private val myRestaurantsViewModel: MyRestaurantsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityMyRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.addRestaurant.setOnClickListener {
            // TODO agregar restaurante creado a la lista
        }


        myRestaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.MINIFIED)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.myRestaurantsList.layoutManager = horizontalLayoutManager
        binding.myRestaurantsList.adapter = myRestaurantsAdapter
        myRestaurantsViewModel.getMyRestaurants()
        myRestaurantsViewModel.myRestaurants.observe(this) {
            myRestaurantsAdapter.setRestaurants(it)
        }
        myRestaurantsViewModel.myRestaurants.observe(this) {
            // TODO capturar loading y mostrar/ocultar skeleton o mostrar un error
        }
    }

    override fun onItemClick(restaurant: RestaurantModel) {
        // TODO ir a editar restaurante
    }
}