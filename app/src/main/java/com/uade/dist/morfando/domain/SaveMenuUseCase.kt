package com.uade.dist.morfando.domain

import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.MenuModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveMenuUseCase(auth: String, restaurantCode: String) {
    private val retrofit = RetrofitHelper.getRetrofit(auth, restaurantCode)
    private val api = retrofit.create(RestaurantApiClient::class.java)

    suspend fun updateMenu(menu: MenuModel): Result<MenuModel> {
        return withContext(Dispatchers.IO) {
            api.updateMenu(menu)
        }
    }

    suspend fun saveMenu(menu: MenuModel): Result<MenuModel> {
        return withContext(Dispatchers.IO) {
            api.saveMenu(menu)
        }
    }
}
