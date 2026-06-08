package com.teamsasa.buonappetito.data.api

import com.teamsasa.buonappetito.data.model.*
import retrofit2.http.*

interface ApiService {
    // Authentification
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    // Menu / Plats
    @GET("menu/popular")
    suspend fun getPopularDishes(): List<Dish>

    @GET("menu")
    suspend fun getAllDishes(@Query("category") category: String?): List<Dish>

    // Commandes (J2: Intégration table_number et commentaires via CheckoutRequest)
    @POST("orders")
    suspend fun createOrder(@Body checkoutRequest: CheckoutRequest): Order

    @GET("orders/history")
    suspend fun getOrderHistory(): List<Order>

    @GET("orders/{id}/track")
    suspend fun trackOrder(@Path("id") orderId: Long): Order
}
