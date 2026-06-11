package com.teamsasa.buonappetito.data.repository

import com.teamsasa.buonappetito.data.api.ApiService
import com.teamsasa.buonappetito.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val apiService: ApiService) {

    suspend fun createOrder(request: CheckoutRequest): Result<Order> = withContext(Dispatchers.IO) {
        runCatching { apiService.createOrder(request) }
    }

    suspend fun getOrderHistory(): Result<List<Order>> = withContext(Dispatchers.IO) {
        runCatching { apiService.getOrderHistory() }
    }

    suspend fun trackOrder(orderId: Long): Result<Order> = withContext(Dispatchers.IO) {
        runCatching { apiService.trackOrder(orderId) }
    }

    // J3 : Ajouter des plats à une commande en cours
    suspend fun addItemsToOrder(orderId: Long, items: List<CartItemRequest>): Result<Order> =
        withContext(Dispatchers.IO) {
            runCatching { apiService.addItemsToOrder(orderId, AddItemsRequest(items)) }
        }

    // J3 : Notation
    suspend fun submitReview(orderId: Long, rating: Int, comment: String): Result<ReviewResponse> =
        withContext(Dispatchers.IO) {
            runCatching { apiService.submitReview(orderId, ReviewRequest(orderId, rating, comment)) }
        }

    // J3 : Stripe
    suspend fun createPaymentIntent(orderId: Long, convives: Int): Result<PaymentIntentResponse> =
        withContext(Dispatchers.IO) {
            runCatching { apiService.createPaymentIntent(PaymentIntentRequest(orderId, convives)) }
        }

    suspend fun confirmPayment(paymentIntentId: String, orderId: Long): Result<PaymentConfirmResponse> =
        withContext(Dispatchers.IO) {
            runCatching { apiService.confirmPayment(ConfirmPaymentRequest(paymentIntentId, orderId)) }
        }

    // J3 : Fidélité
    suspend fun getLoyalty(): Result<LoyaltyResponse> = withContext(Dispatchers.IO) {
        runCatching { apiService.getLoyalty() }
    }

    // J3 : Ticket PDF
    suspend fun getTicketUrl(orderId: Long): Result<TicketUrlResponse> = withContext(Dispatchers.IO) {
        runCatching { apiService.getTicketUrl(orderId) }
    }
}
