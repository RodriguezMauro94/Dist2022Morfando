package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
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

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        binding.addRestaurant.setOnClickListener {
            startActivityForResult(Intent(this, CreateEditRestaurantActivity::class.java), ADD_RESTAURANT_REQUEST_CODE)
        }

        myRestaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.MINIFIED)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.myRestaurantsList.layoutManager = horizontalLayoutManager
        binding.myRestaurantsList.adapter = myRestaurantsAdapter
        myRestaurantsViewModel.getMyRestaurants(token)
        myRestaurantsViewModel.myRestaurants.observe(this) {
            myRestaurantsAdapter.setRestaurants(it)
        }
        myRestaurantsViewModel.myRestaurants.observe(this) {
            // TODO capturar loading y mostrar/ocultar skeleton o mostrar un error
        }
    }

    override fun onItemClick(restaurant: RestaurantModel) {
        val intent = Intent(this, CreateEditRestaurantActivity::class.java)
        intent.putExtra("restaurant", restaurant)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_RESTAURANT_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("restaurant") as RestaurantModel
                val list = myRestaurantsAdapter.getRestaurants().toMutableList()
                list.add(result)
                myRestaurantsAdapter.setRestaurants(list)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}