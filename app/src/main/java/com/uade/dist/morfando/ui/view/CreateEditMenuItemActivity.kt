package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.itemCategories
import com.uade.dist.morfando.core.itemType
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.model.MenuItemModel
import com.uade.dist.morfando.data.model.PlateModel
import com.uade.dist.morfando.databinding.ActivityCreateEditMenuItemBinding
import java.util.*

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
        binding.categorySpinner.adapter = categoriesAdapter

        val typeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, itemType)
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.itemTypeSpinner.adapter = typeAdapter

        binding.save.setOnClickListener {
            val name =  binding.nameValue.text.toString()
            val price =  binding.priceValue.text.toString()
            val description =  binding.descriptionValue.text.toString()

            // TODO validate photo

            if (name.isNotEmpty() && price.isNotEmpty() && description.isNotEmpty()) {
                val isVegan = binding.isVegan.isChecked
                val isCeliac = binding.isCeliac.isChecked

                val plate = PlateModel(
                    UUID.randomUUID().toString(),
                    name,
                    description,
                    price.toDouble(),
                    "https://i.imgur.com/GB7lTPH.jpeg", // TODO foto
                    isVegan,
                    isCeliac
                )

                val menu = MenuItemModel(
                    binding.itemTypeSpinner.selectedItem as String,
                    binding.categorySpinner.selectedItem as String,
                    mutableListOf(
                        plate
                    )
                )

                val intent = Intent()
                intent.putExtra("menuItem", menu)
                setResult(ADD_EDIT_MENU_ITEM_REQUEST_CODE, intent)
                finish()
            } else {
                getString(R.string.error_complete_fields).showToast(this)
            }
        }
    }
}