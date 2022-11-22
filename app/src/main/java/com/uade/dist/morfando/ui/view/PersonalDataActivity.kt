package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.*
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.databinding.ActivityPersonalDataBinding
import com.uade.dist.morfando.domain.UserPersonalDataUseCase
import com.uade.dist.morfando.ui.viewmodel.PersonalDataViewModel

class PersonalDataActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPersonalDataBinding
    private val personalDataViewModel: PersonalDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""
        personalDataViewModel.userPersonalDataUseCase = UserPersonalDataUseCase(token)

        personalDataViewModel.getPersonalData()

        binding.save.setOnClickListener {
            personalDataViewModel.updatePersonalData()
        }

        personalDataViewModel.getRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    updateFields()
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        personalDataViewModel.postRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    getString(R.string.personal_data_updated).showToast(this)
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        binding.nameValue.doAfterTextChanged {
            personalDataViewModel.personalData.value?.apply {
                name = it.toString()
            }
        }

        checkCameraPermission(applicationContext, this)

        binding.photosGroup.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST) {
            data?.apply {
                handleCameraCallback(data) { photo, pathFile ->
                    binding.profilePhoto.setImageBitmap(photo)
                    personalDataViewModel.personalData.value?.apply {
                        image = pathFile
                    }
                }
            }
        }
    }

    private fun updateFields() {
        personalDataViewModel.personalData.value?.apply {
            binding.nameValue.setText(name)
            Picasso.get()
                .load(image)
                .placeholder(R.drawable.logo_morfando)
                .into(binding.profilePhoto)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}