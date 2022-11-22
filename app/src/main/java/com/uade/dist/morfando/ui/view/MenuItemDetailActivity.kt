package com.uade.dist.morfando.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.getVisibility
import com.uade.dist.morfando.databinding.ActivityMenuItemDetailBinding
import com.uade.dist.morfando.ui.view.menuList.PlateItemList

class MenuItemDetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMenuItemDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityMenuItemDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val plate = intent.extras?.getSerializable("plate") as PlateItemList

        Picasso.get()
            .load(plate.image)
            .placeholder(R.drawable.logo_morfando)
            .into(binding.plateLanding)
        binding.plateName.text = plate.name
        binding.plateDescription.text = plate.description
        binding.plateGroup.visibility = (plate.isCeliac || plate.isVegan).getVisibility()
        binding.veganImage.visibility = plate.isVegan.getVisibility()
        binding.veganText.visibility = plate.isVegan.getVisibility()
        binding.celiacImage.visibility = plate.isCeliac.getVisibility()
        binding.celiacText.visibility = plate.isCeliac.getVisibility()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}