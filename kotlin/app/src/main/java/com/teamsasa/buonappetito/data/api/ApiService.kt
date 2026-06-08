package com.teamsasa.buonappetito.data.api

import com.teamsasa.buonappetito.data.model.*
import retrofit2.http.*

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("logout")
    suspend fun logout(): LogoutResponse

    @GET("menu/popular")
    suspend fun getPopularDishes(): List<Dish>

    @GET("menu")
    suspend fun getAllDishes(@Query("category") category: String?): List<Dish>

    @POST("orders")
    suspend fun createOrder(@Body request: CheckoutRequest): Order

    @GET("orders/history")
    suspend fun getOrderHistory(): List<Order>

    @GET("orders/{id}/track")
    suspend fun trackOrder(@Path("id") orderId: Long): Order

    // J3 : Ajouter des plats à une commande en cours
    @POST("orders/{id}/add-items")
    suspend fun addItemsToOrder(
        @Path("id") orderId: Long,
        @Body items: AddItemsRequest
    ): Order

    // J3 : Notation
    @POST("orders/{id}/review")
    suspend fun submitReview(
        @Path("id") orderId: Long,
        @Body request: ReviewRequest
    ): ReviewResponse

    // J3 : Stripe PaymentIntent
    @POST("payments/create-intent")
    suspend fun createPaymentIntent(@Body request: PaymentIntentRequest): PaymentIntentResponse

    @POST("payments/confirm")
    suspend fun confirmPayment(@Body request: ConfirmPaymentRequest): PaymentConfirmResponse

    // J3 : Programme fidélité
    @GET("loyalty")
    suspend fun getLoyalty(): LoyaltyResponse

    // J3 : Ticket PDF
    @GET("orders/{id}/ticket")
    suspend fun getTicketUrl(@Path("id") orderId: Long): TicketUrlResponse
}
