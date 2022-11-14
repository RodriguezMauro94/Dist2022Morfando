package com.uade.dist.morfando.domain

import com.google.gson.annotations.SerializedName
import com.uade.dist.morfando.core.RetrofitHelper
import com.uade.dist.morfando.data.model.MenuModel
import com.uade.dist.morfando.data.model.RestaurantModel
import com.uade.dist.morfando.data.model.SaveMenuModel
import com.uade.dist.morfando.data.network.RestaurantApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Serializable

class SaveMenuUseCase {
    private val retrofit = RetrofitHelper.getRetrofit()
    private val api = retrofit.create(RestaurantApiClient::class.java)

    suspend fun updateMenu(code: String, menu: MenuModel): Result<RestaurantModel> {
        return withContext(Dispatchers.IO) {
            api.updateMenu(SaveMenuModel(code, menu))
        }
    }

    suspend fun saveMenu(code: String, menu: MenuModel): Result<RestaurantModel> {
        return withContext(Dispatchers.IO) {
            api.saveMenu(SaveMenuModel(code, menu))
        }
    }
}
