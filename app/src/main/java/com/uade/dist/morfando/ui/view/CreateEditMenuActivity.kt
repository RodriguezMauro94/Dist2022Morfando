package com.uade.dist.morfando.ui.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityCreateEditMenuBinding
import com.uade.dist.morfando.ui.view.menuList.MenuAdapter
import com.uade.dist.morfando.ui.view.menuList.MenuItemList
import com.uade.dist.morfando.ui.view.menuList.PlateItemList
import com.uade.dist.morfando.ui.viewmodel.CreateEditMenuViewModel

const val ADD_MENU_ITEM_REQUEST_CODE = 6000

class CreateEditMenuActivity: AppCompatActivity(), MenuAdapter.ItemClickListener {
    private lateinit var binding: ActivityCreateEditMenuBinding
    private val createEditMenuViewModel: CreateEditMenuViewModel by viewModels()
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityCreateEditMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val restaurant = intent.extras?.getSerializable("restaurant") as? RestaurantModel?
        if (restaurant != null) {
            createEditMenuViewModel.getMenu(restaurant.code)
        }

        binding.addItem.setOnClickListener {
            startActivityForResult(Intent(this, CreateEditMenuItemActivity::class.java), ADD_MENU_ITEM_REQUEST_CODE)
        }

        createEditMenuViewModel.requestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    // TODO ocultar skeleton
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        menuAdapter = MenuAdapter(this)
        val horizontalLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.menuList.layoutManager = horizontalLayoutManager
        binding.menuList.adapter = menuAdapter
        createEditMenuViewModel.menuList.observe(this) {
            menuAdapter.setMenu(it)
        }
    }

    override fun onItemClick(item: MenuItemList) {
        (item as? PlateItemList)?.let { plate ->
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_edit_delete_menu_item)
            val edit = dialog.findViewById(R.id.edit) as LinearLayout
            val delete = dialog.findViewById(R.id.delete) as LinearLayout

            edit.setOnClickListener {
                // TODO
                "edit".showToast(this)
            }

            delete.setOnClickListener {
                val items = menuAdapter.menuItems.toMutableList()
                items.remove(plate)
                menuAdapter.setMenu(items)
                dialog.dismiss()
            }

            dialog.show()
        }
    }
}