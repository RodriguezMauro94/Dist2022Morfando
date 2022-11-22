package com.uade.dist.morfando.core

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.io.ByteArrayOutputStream


const val CAMERA_REQUEST = 1888

fun checkCameraPermission(context: Context, activity: AppCompatActivity) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST)
    }
}

fun handleCameraCallback(context: Context, image: Uri, callback: (photo: Bitmap, pathFile: String) -> Unit) {
    val photo = MediaStore.Images.Media.getBitmap(context.contentResolver, image)

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

fun handleCameraCallback(data: Intent, callback: (photo: Bitmap, pathFile: String) -> Unit) {
    val photo: Bitmap = data.extras?.get("data") as Bitmap

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