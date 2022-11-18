package com.uade.dist.morfando.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.uade.dist.morfando.R
import com.uade.dist.morfando.core.RequestState
import com.uade.dist.morfando.core.showToast
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityMenuBinding
import com.uade.dist.morfando.ui.view.menuList.MenuAdapter
import com.uade.dist.morfando.ui.view.menuList.MenuItemList
import com.uade.dist.morfando.ui.view.menuList.PlateItemList
import com.uade.dist.morfando.ui.viewmodel.MenuViewModel

class MenuActivity: AppCompatActivity(), MenuAdapter.ItemClickListener {
    private lateinit var binding: ActivityMenuBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Morfando)
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val restaurant = intent.extras?.getSerializable("restaurant") as RestaurantModel
        binding.menuTitle.text = getString(R.string.menu_title_activity, restaurant.name)
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""
        menuViewModel.getMenu(token, restaurant.code)

        menuViewModel.requestState.observe(this) {
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
        menuViewModel.menuList.observe(this) {
            menuAdapter.setMenu(it)
        }
    }

    override fun onItemClick(item: MenuItemList) {
        (item as? PlateItemList)?.let {
            val intent = Intent(this, MenuItemDetail::class.java)
            intent.putExtra("plate", item)
            startActivity(intent)
        }
    }
}