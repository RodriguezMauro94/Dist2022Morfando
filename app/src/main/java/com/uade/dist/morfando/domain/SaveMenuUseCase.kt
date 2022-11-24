package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.MenuModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SaveMenuModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMenuUseCase(private val auth: String) {
    private val retrofit = RetrofitHelper.getRetrofit(auth)
    private val api = retrofit.create(RestaurantApiClient::class.java)

    suspend fun updateMenu(code: String, menu: MenuModel): Result<RestaurantModel> {
        return withContext(Dispatchers.IO) {
            api.updateMenu(SaveMenuModel(code, menu.menu))
        }
    }

    suspend fun saveMenu(code: String, menu: MenuModel): Result<RestaurantModel> {
        return withContext(Dispatchers.IO) {
            api.saveMenu(SaveMenuModel(code, menu.menu))
        }
    }
}
