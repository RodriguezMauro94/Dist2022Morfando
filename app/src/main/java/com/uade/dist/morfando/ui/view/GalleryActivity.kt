package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.data.model.RestaurantDetailsModel
import com.uade.dist.morfando.databinding.ActivityGalleryBinding
import com.uade.dist.morfando.ui.view.gallery.GalleryAdapter

class GalleryActivity: AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val details = intent.extras?.getSerializable("details") as RestaurantDetailsModel

        details.images?.let {
            val adapter = GalleryAdapter(this)
            binding.galleryGrid.adapter = adapter
            adapter.images = it
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}