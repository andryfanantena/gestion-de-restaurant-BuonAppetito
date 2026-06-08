package com.teamsasa.buonappetito.data.repository

import com.teamsasa.buonappetito.data.api.ApiService
import com.teamsasa.buonappetito.data.model.Dish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MenuRepository(private val apiService: ApiService) {

    suspend fun getPopularDishes(): Result<List<Dish>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPopularDishes()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllDishes(category: String?): Result<List<Dish>> = withContext(Dispatchers.IO) {
        try {
            val filter = if (category == "All") null else category
            val response = apiService.getAllDishes(filter)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
