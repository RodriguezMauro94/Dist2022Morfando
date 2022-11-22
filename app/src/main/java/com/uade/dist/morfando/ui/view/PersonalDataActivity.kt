package com.uade.dist.morfando.ui.view

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
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
import java.io.File
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.app.ActivityCompat





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

        val permissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE, permissions) -> {
                    openImageIntent()
                }
                else -> {
                    // FIXME hacer algo
                    Toast.makeText(this, "permiso denegado", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.photosGroup.setOnClickListener {
            val check =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (check == PackageManager.PERMISSION_GRANTED) {
                openImageIntent()
            } else {
                permissionRequest.launch(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
    }

    private fun permissionGranted(permission: String, permissions: Map<String, @JvmSuppressWildcards Boolean>)
            = permissions.containsKey(permission) && permissions.getValue(permission)

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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
    }*/

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


    private var outputFileUri: Uri? = null

    private fun openImageIntent() {
        // Determine Uri of camera image to save.
        val root =
            File("" + Environment.getExternalStorageDirectory() + File.separator.toString() + "Morfando" + File.separator)
        root.mkdirs()
        val fname: String = "img_"+ System.currentTimeMillis() + ".jpg"
        val sdImageMainDirectory = File(root, fname)
        outputFileUri = Uri.fromFile(sdImageMainDirectory)

        // Camera.
        val cameraIntents = listOf(Intent(MediaStore.ACTION_IMAGE_CAPTURE))

        // Filesystem.
        val galleryIntent = Intent()
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT

        // Chooser of filesystem options.
        val chooserIntent = Intent.createChooser(galleryIntent, "Agregar foto")

        // Add the camera options.
        chooserIntent.putExtra(
            Intent.EXTRA_INITIAL_INTENTS,
            cameraIntents.toTypedArray<Parcelable>()
        )
        startActivityForResult(chooserIntent, CAMERA_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                val isCamera: Boolean = (data?.extras != null && data.extras!!.containsKey("data"))
                if (isCamera) {
                    handleCameraCallback(data!!) { photo, pathFile ->
                        updatePhoto(photo, pathFile)
                    }
                } else {
                    handleCameraCallback(this, data!!.data!!) { photo, pathFile ->
                        updatePhoto(photo, pathFile)
                    }
                }
            }
        }
    }

    private fun updatePhoto(photo: Bitmap, pathFile: String) {
        binding.profilePhoto.setImageBitmap(photo)
        personalDataViewModel.personalData.value?.apply {
            image = pathFile
        }
    }
}