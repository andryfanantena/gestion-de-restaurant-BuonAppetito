package com.teamsasa.buonappetito.data.repository

import com.teamsasa.buonappetito.data.api.ApiService
import com.teamsasa.buonappetito.data.model.CartItemRequest
import com.teamsasa.buonappetito.data.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val apiService: ApiService) {

    suspend fun createOrder(items: List<CartItemRequest>): Result<Order> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createOrder(items)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrderHistory(token: String): Result<List<Order>> = withContext(Dispatchers.IO) {
        try {
            val formattedToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = apiService.getOrderHistory(formattedToken)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun trackOrder(orderId: Long): Result<Order> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.trackOrder(orderId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
