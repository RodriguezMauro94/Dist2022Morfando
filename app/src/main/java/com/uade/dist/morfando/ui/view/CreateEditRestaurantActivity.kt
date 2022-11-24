package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.*
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.*
import com.uade.dist.morfando.databinding.ActivityCreateEditRestaurantBinding
import com.uade.dist.morfando.ui.view.home.categories.categories
import com.uade.dist.morfando.ui.viewmodel.CreateEditRestaurantViewModel

const val CREATE_MENU_REQUEST_CODE = 5000

class CreateEditRestaurantActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditRestaurantBinding
    private val createEditRestaurantViewModel: CreateEditRestaurantViewModel by viewModels()
    private val photos = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        createEditRestaurantViewModel.token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        val restaurant = intent.extras?.getSerializable("restaurant") as? RestaurantModel?
        createEditRestaurantViewModel.originalRestaurant.postValue(restaurant)
        if (restaurant != null) {
            binding.delete.visibility = View.VISIBLE
            updateStatusButtons(restaurant)
        }

        val priceRangeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, priceRange.values.toList())
        priceRangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.priceRangeSpinner.adapter = priceRangeAdapter

        binding.addEditMenuTitle.text = if (restaurant == null) getString(R.string.add_menu) else getString(R.string.edit_menu)

        setSpinnerArray(binding.mondayOpenHourSpinner, R.array.open_hours_array)
        setSpinnerArray(binding.tuesdayOpenHourSpinner, R.array.open_hours_array)
        setSpinnerArray(binding.wednesdayOpenHourSpinner, R.array.open_hours_array)
        setSpinnerArray(binding.thursdayOpenHourSpinner, R.array.open_hours_array)
        setSpinnerArray(binding.fridayOpenHourSpinner, R.array.open_hours_array)
        setSpinnerArray(binding.saturdayOpenHourSpinner, R.array.open_hours_array)
        setSpinnerArray(binding.sundayOpenHourSpinner, R.array.open_hours_array)

        setSpinnerArray(binding.mondayCloseHourSpinner, R.array.close_hours_array)
        setSpinnerArray(binding.tuesdayCloseHourSpinner, R.array.close_hours_array)
        setSpinnerArray(binding.wednesdayCloseHourSpinner, R.array.close_hours_array)
        setSpinnerArray(binding.thursdayCloseHourSpinner, R.array.close_hours_array)
        setSpinnerArray(binding.fridayCloseHourSpinner, R.array.close_hours_array)
        setSpinnerArray(binding.saturdayCloseHourSpinner, R.array.close_hours_array)
        setSpinnerArray(binding.sundayCloseHourSpinner, R.array.close_hours_array)

        setSpinnerArray(binding.restaurantState, R.array.states)
        setSpinnerArray(binding.restaurantCountry, R.array.countries)

        val categoriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.map {
                getString(it.text)
            })
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.cookingTypeSpinner.adapter = categoriesAdapter

        if (restaurant != null) {
            fillRestaurantInfo(restaurant)
        }

        bindIsOpen(binding.mondayIsOpen, binding.mondayOpenHourSpinner, binding.mondayCloseHourSpinner)
        bindIsOpen(binding.tuesdayIsOpen, binding.tuesdayOpenHourSpinner, binding.tuesdayCloseHourSpinner)
        bindIsOpen(binding.wednesdayIsOpen, binding.wednesdayOpenHourSpinner, binding.wednesdayCloseHourSpinner)
        bindIsOpen(binding.thursdayIsOpen, binding.thursdayOpenHourSpinner, binding.thursdayCloseHourSpinner)
        bindIsOpen(binding.fridayIsOpen, binding.fridayOpenHourSpinner, binding.fridayCloseHourSpinner)
        bindIsOpen(binding.saturdayIsOpen, binding.saturdayOpenHourSpinner, binding.saturdayCloseHourSpinner)
        bindIsOpen(binding.sundayIsOpen, binding.sundayOpenHourSpinner, binding.sundayCloseHourSpinner)

        binding.menuGroup.setOnClickListener {
            val intent = Intent(this, CreateEditMenuActivity::class.java)
            val menu = createEditRestaurantViewModel.menu.value
            if (menu != null && menu.menu.isNotEmpty()) {
                intent.putExtra("menu", menu)
            }else if (restaurant != null) {
                intent.putExtra("restaurant", restaurant)
            }
            startActivityForResult(intent, CREATE_MENU_REQUEST_CODE)
        }

        binding.close.setOnClickListener {
            createEditRestaurantViewModel.updateStatus("closed")
        }

        binding.open.setOnClickListener {
            createEditRestaurantViewModel.updateStatus("active")
        }

        binding.delete.setOnClickListener {
            createEditRestaurantViewModel.deleteRestaurant()
        }

        checkCameraPermission(applicationContext, this)

        binding.photosGroup.setOnClickListener {
            openImageIntent(this)
        }

        binding.save.setOnClickListener {
            val name: String = binding.nameValue.text.toString()
            val aboutUs: String = binding.aboutUsValue.text.toString()
            val street = binding.streetValue.text.toString()
            val streetNumber = binding.streetNumberValue.text.toString()
            val state = binding.restaurantState.selectedItem as String
            val neighborhood = binding.neighborhoodValue.text.toString()
            val town = binding.townValue.text.toString()
            val country = binding.restaurantCountry.selectedItem as String

            val mondayOpenHoursFilled = openHourIsFilled(binding.mondayIsOpen, binding.mondayOpenHourSpinner, binding.mondayCloseHourSpinner)
            val tuesdayOpenHoursFilled = openHourIsFilled(binding.tuesdayIsOpen, binding.tuesdayOpenHourSpinner, binding.tuesdayCloseHourSpinner)
            val wednesdayOpenHoursFilled = openHourIsFilled(binding.wednesdayIsOpen, binding.wednesdayOpenHourSpinner, binding.wednesdayCloseHourSpinner)
            val thursdayOpenHoursFilled = openHourIsFilled(binding.thursdayIsOpen, binding.thursdayOpenHourSpinner, binding.thursdayCloseHourSpinner)
            val fridayOpenHoursFilled = openHourIsFilled(binding.fridayIsOpen, binding.fridayOpenHourSpinner, binding.fridayCloseHourSpinner)
            val saturdayOpenHoursFilled = openHourIsFilled(binding.saturdayIsOpen, binding.saturdayOpenHourSpinner, binding.saturdayCloseHourSpinner)
            val sundayOpenHoursFilled = openHourIsFilled(binding.sundayIsOpen, binding.sundayOpenHourSpinner, binding.sundayCloseHourSpinner)

            val priceRange = binding.priceRangeSpinner.selectedItemPosition + 1
            val menuIsValid = if (restaurant == null) createEditRestaurantViewModel.menu.value != null else true

            if (
                name.isNotEmpty() &&
                aboutUs.isNotEmpty() &&
                street.isNotEmpty() &&
                streetNumber.isNotEmpty() &&
                state.isNotEmpty() &&
                neighborhood.isNotEmpty() &&
                town.isNotEmpty() &&
                country.isNotEmpty() &&
                mondayOpenHoursFilled &&
                tuesdayOpenHoursFilled &&
                wednesdayOpenHoursFilled &&
                thursdayOpenHoursFilled &&
                fridayOpenHoursFilled &&
                saturdayOpenHoursFilled &&
                sundayOpenHoursFilled &&
                menuIsValid &&
                photos.isNotEmpty()
            ) {
                val direction = "$street $streetNumber, $neighborhood, $town, $state, $country"
                var latitude = 0.0
                var longitude = 0.0

                val geocoder = Geocoder(this)
                val addresses = geocoder.getFromLocationName(direction, 1)
                if(addresses.isNotEmpty()) {
                    latitude = addresses[0].latitude
                    longitude = addresses[0].longitude
                }

                val createRestaurantModel = CreateRestaurantModel(
                    null,
                    name,
                    aboutUs,
                    street,
                    streetNumber.toInt(),
                    state,
                    neighborhood,
                    town,
                    country,
                    OpenHoursModel(
                        getOpenHours("monday", binding.mondayIsOpen, binding.mondayOpenHourSpinner, binding.mondayCloseHourSpinner),
                        getOpenHours("tuesday", binding.tuesdayIsOpen, binding.tuesdayOpenHourSpinner, binding.tuesdayCloseHourSpinner),
                        getOpenHours("wednesday", binding.wednesdayIsOpen, binding.wednesdayOpenHourSpinner, binding.wednesdayCloseHourSpinner),
                        getOpenHours("thursday", binding.thursdayIsOpen, binding.thursdayOpenHourSpinner, binding.thursdayCloseHourSpinner),
                        getOpenHours("friday", binding.fridayIsOpen, binding.fridayOpenHourSpinner, binding.fridayCloseHourSpinner),
                        getOpenHours("saturday", binding.saturdayIsOpen, binding.saturdayOpenHourSpinner, binding.saturdayCloseHourSpinner),
                        getOpenHours("sunday", binding.sundayIsOpen, binding.sundayOpenHourSpinner, binding.sundayCloseHourSpinner),
                    ),
                    categories[binding.cookingTypeSpinner.selectedItemPosition].id,
                    priceRange,
                    photos,
                    latitude,
                    longitude,
                    null
                )

                if (restaurant == null) {
                    createRestaurantModel.status = "active"
                    createEditRestaurantViewModel.createRestaurant(createRestaurantModel)
                } else {
                    createRestaurantModel.code = restaurant.code
                    createEditRestaurantViewModel.editRestaurant(createRestaurantModel)
                }
            } else {
                getString(R.string.error_complete_fields).showToast(this)
            }
        }

        createEditRestaurantViewModel.createRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    // TODO devolver restaurant as result
                    finish()
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        createEditRestaurantViewModel.editRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.edited_restaurant_status).showToast(this)
                    finish()
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        createEditRestaurantViewModel.updateStatusRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.updated_restaurant_status).showToast(this)
                    createEditRestaurantViewModel.originalRestaurant.value?.apply {
                        updateStatusButtons(this)
                    }
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        createEditRestaurantViewModel.deleteRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.deleted_restaurant_status).showToast(this)
                    finish()
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }
    }

    private fun fillRestaurantInfo(restaurant: RestaurantModel) {
        binding.loading.visibility = View.GONE
        binding.editMain.visibility = View.VISIBLE
        binding.nameValue.setText(restaurant.name)
        binding.aboutUsValue.setText(restaurant.aboutUs)
        binding.streetValue.setText(restaurant.streetValue)
        binding.streetNumberValue.setText(restaurant.streetNumberValue.toString())
        binding.neighborhoodValue.setText(restaurant.neighborhood)
        binding.townValue.setText(restaurant.townValue)

        showOpenHour(restaurant.openHours.monday, binding.mondayIsOpen, binding.mondayOpenHourSpinner, binding.mondayCloseHourSpinner)
        showOpenHour(restaurant.openHours.tuesday, binding.tuesdayIsOpen, binding.tuesdayOpenHourSpinner, binding.tuesdayCloseHourSpinner)
        showOpenHour(restaurant.openHours.wednesday, binding.wednesdayIsOpen, binding.wednesdayOpenHourSpinner, binding.wednesdayCloseHourSpinner)
        showOpenHour(restaurant.openHours.thursday, binding.thursdayIsOpen, binding.thursdayOpenHourSpinner, binding.thursdayCloseHourSpinner)
        showOpenHour(restaurant.openHours.friday, binding.fridayIsOpen, binding.fridayOpenHourSpinner, binding.fridayCloseHourSpinner)
        showOpenHour(restaurant.openHours.saturday, binding.saturdayIsOpen, binding.saturdayOpenHourSpinner, binding.saturdayCloseHourSpinner)
        showOpenHour(restaurant.openHours.sunday, binding.sundayIsOpen, binding.sundayOpenHourSpinner, binding.sundayCloseHourSpinner)

        binding.restaurantState.setSelection(resources.getStringArray(R.array.states).indexOf(restaurant.stateValue))
        binding.restaurantCountry.setSelection(resources.getStringArray(R.array.countries).indexOf(restaurant.stateValue))

        binding.cookingTypeSpinner.setSelection(categories.indexOfFirst { category ->
            category.id == restaurant.cookingType
        })

        binding.priceRangeSpinner.setSelection(restaurant.priceRange - 1)

        restaurant.images?.let { images ->
            photos.addAll(images)
        }
    }

    private fun updateStatusButtons(restaurant: RestaurantModel) {
        if (restaurant.status == "active") {
            binding.close.visibility = View.VISIBLE
        } else {
            binding.open.visibility = View.VISIBLE
        }
    }

    private fun showOpenHour(openHoursModel: OpenHoursDayModel, isOpen: CheckBox, openHourSpinner: Spinner, closeHourSpinner: Spinner) {
        val open = resources.getStringArray(R.array.open_hours_array)
        val closed = resources.getStringArray(R.array.close_hours_array)

        openHoursModel.openHours?.let {
            openHourSpinner.setSelection(open.indexOf(it))
        }
        openHoursModel.closeHours?.let {
            closeHourSpinner.setSelection(closed.indexOf(it))
        }

        isOpen.isChecked = openHoursModel.isOpen
    }

    private fun getOpenHours(day: String, isOpen: CheckBox, openHourSpinner: Spinner, closeHourSpinner: Spinner): OpenHoursDayModel {
        return if (isOpen.isChecked) {
            OpenHoursDayModel(
                day,
                openHourSpinner.selectedItem as String,
                closeHourSpinner.selectedItem as String,
                isOpen.isChecked
            )
        } else {
            OpenHoursDayModel(
                day,
                null,
                null,
                isOpen.isChecked
            )
        }
    }

    private fun openHourIsFilled(isOpen: CheckBox, openHourSpinner: Spinner, closeHourSpinner: Spinner): Boolean {
        return (isOpen.isChecked &&
                openHourSpinner.selectedItemPosition != 0 &&
                closeHourSpinner.selectedItemPosition != 0) || !isOpen.isChecked
    }

    private fun bindIsOpen(isOpen: CheckBox, openHourSpinner: Spinner, closeHourSpinner: Spinner) {
        isOpen.setOnCheckedChangeListener { _, checked ->
            openHourSpinner.isEnabled = checked
            closeHourSpinner.isEnabled = checked
            if (!checked) {
                openHourSpinner.setSelection(0)
                closeHourSpinner.setSelection(0)
            }
        }
    }

    private fun setSpinnerArray(spinner: Spinner, array: Int) {
        ArrayAdapter.createFromResource(
            this,
            array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_MENU_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("menu") as MenuModel
                createEditRestaurantViewModel.menu.value = result
            }
        } else if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                handleCameraCallback(this, data) { _, pathFile ->
                    photos.add(pathFile)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}