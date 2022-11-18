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
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_NAME
import com.uade.dist.morfando.data.local.SHARED_PREFERENCES_TOKEN
import com.uade.dist.morfando.data.model.MenuItemModel
import com.uade.dist.morfando.data.model.MenuModel
import com.uade.dist.morfando.data.model.PlateModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.databinding.ActivityCreateEditMenuBinding
import com.uade.dist.morfando.ui.view.menuList.MenuAdapter
import com.uade.dist.morfando.ui.view.menuList.MenuItemList
import com.uade.dist.morfando.ui.view.menuList.PlateItemList
import com.uade.dist.morfando.ui.view.menuList.toViewList
import com.uade.dist.morfando.ui.viewmodel.CreateEditMenuViewModel

const val ADD_MENU_ITEM_REQUEST_CODE = 6000
const val EDIT_MENU_ITEM_REQUEST_CODE = 7000

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

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        createEditMenuViewModel.token = sharedPreferences.getString(SHARED_PREFERENCES_TOKEN, null) ?: ""

        val restaurant = intent.extras?.getSerializable("restaurant") as? RestaurantModel?
        if (restaurant != null) {
            createEditMenuViewModel.getMenu(restaurant.code)
        }

        binding.addItem.setOnClickListener {
            startActivityForResult(Intent(this, CreateEditMenuItemActivity::class.java), ADD_MENU_ITEM_REQUEST_CODE)
        }

        createEditMenuViewModel.getMenuRequestState.observe(this) {
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
        createEditMenuViewModel.menuViewList.observe(this) {
            menuAdapter.setMenu(it)
        }
        createEditMenuViewModel.getMenuRequestState.observe(this) {
            // TODO mostrar/ocultar skeleton
        }

        createEditMenuViewModel.saveMenuRequestState.observe(this) {
            when (it) {
                is RequestState.LOADING -> {
                    getString(R.string.loading).showToast(this)
                }
                is RequestState.SUCCESS -> {
                    finish()
                }
                is RequestState.FAILURE -> {
                    getString(R.string.generic_error).showToast(this)
                }
            }
        }

        binding.save.setOnClickListener {
            if (restaurant != null) {
                createEditMenuViewModel.saveMenu(restaurant.code)
            } else {
                if (!createEditMenuViewModel.menuLogicList.value.isNullOrEmpty()) {
                    val intent = Intent()
                    intent.putExtra("menu", MenuModel(createEditMenuViewModel.menuLogicList.value!!))
                    setResult(CREATE_MENU_REQUEST_CODE, intent)
                }

                finish()
            }
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
                var plateModel: PlateModel? = null
                var menuModel: MenuItemModel? = null

                createEditMenuViewModel.menuLogicList.value!!.forEach { menuList ->
                    menuList.plates.forEach { plateList ->
                        if(plateList.code == plate.code) {
                            plateModel = plateList
                            menuModel = menuList
                        }
                    }
                }

                plateModel?.apply {
                    val intent = Intent(this@CreateEditMenuActivity, CreateEditMenuItemActivity::class.java)
                    intent.putExtra("plate", plateModel)
                    intent.putExtra("menu", menuModel)
                    startActivityForResult(intent, EDIT_MENU_ITEM_REQUEST_CODE)
                }
                dialog.dismiss()
            }

            delete.setOnClickListener {
                val items = createEditMenuViewModel.menuLogicList.value!!.toMutableList()
                items.forEach { menuModel ->
                    menuModel.plates.removeAll {  plateModel ->
                        plateModel.code == plate.code
                    }
                }
                createEditMenuViewModel.menuLogicList.value = items
                menuAdapter.setMenu(items.toViewList())
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_MENU_ITEM_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("menuItem") as MenuItemModel
                val logicList = createEditMenuViewModel.menuLogicList.value!!.toMutableList()
                addPlate(logicList, result)
            }
        } else if (requestCode == EDIT_MENU_ITEM_REQUEST_CODE) {
            data?.apply {
                val result = this.getSerializableExtra("menuItem") as MenuItemModel
                val logicList = createEditMenuViewModel.menuLogicList.value!!.toMutableList()
                logicList.forEach {
                    it.plates.removeAll { plates ->
                        plates.code == result.plates[0].code
                    }
                }
                addPlate(logicList, result)
            }
        }
    }

    private fun addPlate(logicList: MutableList<MenuItemModel>, result: MenuItemModel) {
        val menuItem: MenuItemModel? = logicList.find {
            it.type == result.type && it.category == result.category
        }
        if (menuItem != null) {
            menuItem.plates.addAll(result.plates)
        } else {
            logicList.add(result)
        }

        createEditMenuViewModel.menuLogicList.value = logicList
        menuAdapter.setMenu(logicList.toViewList())
    }
}