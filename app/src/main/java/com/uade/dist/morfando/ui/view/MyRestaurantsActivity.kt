package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
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
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityMyRestaurantsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        binding.addRestaurant.setOnClickListener {
            startActivityForResult(Intent(this, CreateEditRestaurantActivity::class.java), ADD_RESTAURANT_REQUEST_CODE)
        }

        myRestaurantsAdapter = RestaurantsAdapter(this, RestaurantViewMode.MINIFIED)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.myRestaurantsList.layoutManager = horizontalLayoutManager
        binding.myRestaurantsList.adapter = myRestaurantsAdapter
        myRestaurantsViewModel.myRestaurants.observe(this) {
            myRestaurantsAdapter.setRestaurants(it)
            if (it.isEmpty()) {
                binding.empty.visibility = View.VISIBLE
            }
        }

        myRestaurantsViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.myRestaurantsList.visibility = View.GONE
                }
                is RequestState.SUCCESS -> {
                    binding.loading.visibility = View.GONE
                    binding.myRestaurantsList.visibility = View.VISIBLE
                }
                is RequestState.FAILURE -> {
                    binding.loading.visibility = View.GONE
                    binding.myRestaurantsList.visibility = View.GONE
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        myRestaurantsViewModel.getMyRestaurants(token)
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