package com.uade.dist.morfando.ui.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.itemCategories
import com.uade.dist.morfando.core.itemType
import com.uade.dist.morfando.databinding.ActivityCreateEditMenuItemBinding

class CreateEditMenuItemActivity: AppCompatActivity() {
    private lateinit var binding: ActivityCreateEditMenuItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditMenuItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categoriesAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemCategories)
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.cookingTypeSpinner.adapter = categoriesAdapter

        val typeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemType)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.itemTypeSpinner.adapter = typeAdapter
    }
}