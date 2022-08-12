package com.uade.dist.morfando.model

data class RestaurantModel(val name: String, val speciality: String)

class RestaurantProvider {
    companion object {
        fun random(): RestaurantModel {
            return restaurants.random()
        }

        private val restaurants = listOf(
            RestaurantModel("Ristorante Italiano", "Pasta"),
            RestaurantModel("El japonés", "Sushi"),
            RestaurantModel("NY Slice", "Pizza")
        )
    }
}