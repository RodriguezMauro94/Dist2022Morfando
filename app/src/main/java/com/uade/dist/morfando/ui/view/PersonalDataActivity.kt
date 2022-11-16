package com.uade.dist.morfando.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.widget.doAfterTextChanged
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.databinding.ActivityPersonalDataBinding
import com.uade.dist.morfando.ui.viewmodel.PersonalDataViewModel
import java.io.ByteArrayOutputStream
import java.io.File


class PersonalDataActivity: AppCompatActivity() {
    private lateinit var binding: ActivityPersonalDataBinding
    private val personalDataViewModel: PersonalDataViewModel by viewModels()
    private val cameraRequest = 1888

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        //TODO foto
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        }

        binding.photosGroup.setOnClickListener {
            //val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            //getIntent.type = "image/*"

            //val pickIntent =
            //    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //pickIntent.type = "image/*"

            /*val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startActivityForResult(chooserIntent, 1)*/

            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            binding.profilePhoto.setImageBitmap(photo)

            val stream = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.PNG, 90, stream)

            //val image = stream.toByteArray()
            uploadToCloudinary(stream.toByteArray())
        }
    }

/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            data?.data?.apply {
                val file = File(data.data.toString())


                uploadToCloudinary(this)
            }
        }
    }
*/
    private fun uploadToCloudinary(bitmap: ByteArray) {
        val config: HashMap<String, String> = HashMap()
        config["cloud_name"] = "de7zrcqyz"
        config["api_key"] = "114862491191135"
        config["api_secret"] = "lQ7HA9Yh9kJiPHhC6Eef_zowfGo"
        MediaManager.init(this, config)

        MediaManager.get().upload(bitmap).unsigned("vy9oyliy").callback(object : UploadCallback {
            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                "Task successful".showToast(this@PersonalDataActivity)
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {
            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                ("Task Not successful"+ error).showToast(this@PersonalDataActivity)
            }

            override fun onStart(requestId: String?) {
                "Start".showToast(this@PersonalDataActivity)
            }
        }).dispatch()
    }

    private fun updateFields() {
        personalDataViewModel.personalData.value?.apply {
            binding.nameValue.setText(name)
            //TODO foto
        }
    }
}