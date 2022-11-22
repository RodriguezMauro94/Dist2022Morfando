package com.uade.dist.morfando.core

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.io.ByteArrayOutputStream
import java.io.File

const val CAMERA_REQUEST = 1888

fun checkCameraPermission(context: Context, activity: AppCompatActivity) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST)
    }
}

fun openImageIntent(activity: AppCompatActivity) {
    val root =
        File("" + Environment.getExternalStorageDirectory() + File.separator.toString() + "Morfando" + File.separator)
    root.mkdirs()

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
    activity.startActivityForResult(chooserIntent, CAMERA_REQUEST)
}

fun handleCameraCallback(context: Context, image: Uri, callback: (photo: Bitmap, pathFile: String) -> Unit) {
    val photo = MediaStore.Images.Media.getBitmap(context.contentResolver, image)
    compressAndPost(photo, callback)
}

fun handleCameraCallback(data: Intent, callback: (photo: Bitmap, pathFile: String) -> Unit) {
    val photo: Bitmap = data.extras?.get("data") as Bitmap
    compressAndPost(photo, callback)
}

private fun compressAndPost(
    photo: Bitmap,
    callback: (photo: Bitmap, pathFile: String) -> Unit
) {
    val stream = ByteArrayOutputStream()
    photo.compress(Bitmap.CompressFormat.PNG, 90, stream)
    val bitmap = stream.toByteArray()

    MediaManager.get().upload(bitmap).unsigned("vy9oyliy").callback(object : UploadCallback {
        override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
            callback(photo, resultData?.get("url") as String)
        }

        override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
        }

        override fun onReschedule(requestId: String?, error: ErrorInfo?) {
        }

        override fun onError(requestId: String?, error: ErrorInfo?) {
        }

        override fun onStart(requestId: String?) {
        }
    }).dispatch()
}

fun initCloudinary(context: Context) {
    val config: HashMap<String, String> = HashMap()
    config["cloud_name"] = "de7zrcqyz"
    config["api_key"] = "114862491191135"
    config["api_secret"] = "lQ7HA9Yh9kJiPHhC6Eef_zowfGo"
    MediaManager.init(context, config)
}