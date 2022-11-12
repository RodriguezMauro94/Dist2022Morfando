package com.uade.dist.morfando.ui.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityCreateEditRestaurantBinding
import com.uade.dist.morfando.ui.view.home.categories.categories
import com.uade.dist.morfando.ui.viewmodel.CreateEditRestaurantViewModel

class CreateEditRestaurantActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditRestaurantBinding
    private val createEditRestaurantViewModel: CreateEditRestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val restaurant = intent.extras?.getSerializable("restaurant") as? RestaurantModel?
        createEditRestaurantViewModel.originalRestaurant.postValue(restaurant)

        binding.addEditMenuTitle.text = if (restaurant == null) getString(R.string.add_menu) else getString(R.string.edit_menu)

        setHoursAdapter(binding.mondayOpenHourSpinner, R.array.open_hours_array)
        setHoursAdapter(binding.tuesdayOpenHourSpinner, R.array.open_hours_array)
        setHoursAdapter(binding.wednesdayOpenHourSpinner, R.array.open_hours_array)
        setHoursAdapter(binding.thursdayOpenHourSpinner, R.array.open_hours_array)
        setHoursAdapter(binding.fridayOpenHourSpinner, R.array.open_hours_array)
        setHoursAdapter(binding.saturdayOpenHourSpinner, R.array.open_hours_array)
        setHoursAdapter(binding.sundayOpenHourSpinner, R.array.open_hours_array)

        setHoursAdapter(binding.mondayCloseHourSpinner, R.array.close_hours_array)
        setHoursAdapter(binding.tuesdayCloseHourSpinner, R.array.close_hours_array)
        setHoursAdapter(binding.wednesdayCloseHourSpinner, R.array.close_hours_array)
        setHoursAdapter(binding.thursdayCloseHourSpinner, R.array.close_hours_array)
        setHoursAdapter(binding.fridayCloseHourSpinner, R.array.close_hours_array)
        setHoursAdapter(binding.saturdayCloseHourSpinner, R.array.close_hours_array)
        setHoursAdapter(binding.sundayCloseHourSpinner, R.array.close_hours_array)

        val categoriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.map {
                getString(it.text)
            })
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.kitchenStyleSpinner.adapter = categoriesAdapter

        /*
            binding.nameValue

            binding.streetValue
            binding.streetNumberValue
            binding.stateValue
            binding.neighborhoodValue
            binding.townValue
            binding.stateValue
            binding.countryValue

            binding.mondayIsOpen
            binding.tuesdayIsOpen
            binding.wednesdayIsOpen
            binding.thursdayIsOpen
            binding.fridayIsOpen
            binding.saturdayIsOpen
            binding.sundayIsOpen
        */

        binding.menuGroup.setOnClickListener {
            // TODO
        }

        binding.photosGroup.setOnClickListener {
            // TODO
        }

        binding.save.setOnClickListener {
            // TODO
        }
    }

    private fun setHoursAdapter(spinner: Spinner, array: Int) {
        ArrayAdapter.createFromResource(
            this,
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }
}