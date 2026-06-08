package com.teamsasa.buonappetito.data.repository

import com.teamsasa.buonappetito.data.api.ApiService
import com.teamsasa.buonappetito.data.model.CheckoutRequest
import com.teamsasa.buonappetito.data.model.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val apiService: ApiService) {

    suspend fun createOrder(request: CheckoutRequest): Result<Order> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createOrder(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getOrderHistory(): Result<List<Order>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getOrderHistory()
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
