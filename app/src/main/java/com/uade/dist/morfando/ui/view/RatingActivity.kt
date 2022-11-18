package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.RatingModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityRatingBinding
import com.uade.dist.morfando.ui.viewmodel.RatingViewModel

const val RATING_REQUEST_CODE = 3000

class RatingActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRatingBinding
    private val ratingViewModel: RatingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val restaurant = intent.extras?.getSerializable("restaurant") as RestaurantModel

        binding.ratingPublish.setOnClickListener {
            val title: String = binding.editTextTitle.text.toString()
            val description: String = binding.editTextDescription.text.toString()
            val rating = binding.rating.rating

            val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

            if (rating == 0.0.toFloat() || title.isEmpty() || description.isEmpty()) {
                getString(R.string.error_complete_fields).showToast(this)
            } else {
                ratingViewModel.publish(
                    token,
                    RatingModel(
                        restaurant.code,
                        rating.toLong(),
                        title,
                        description
                    ) // FIXME enviar imagen del user (se usa para callback no para el back)
                )
            }
        }

        ratingViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    ratingViewModel.rating?.let {
                        intent.putExtra("rating", ratingViewModel.rating)
                        setResult(RATING_REQUEST_CODE, intent)
                        finish()
                    }
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }
    }
}