package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamsasa.buonappetito.data.api.ApiService
import com.teamsasa.buonappetito.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val apiService: ApiService) : ViewModel() {

    private val _trackedOrder = MutableStateFlow<Order?>(null)
    val trackedOrder: StateFlow<Order?> = _trackedOrder.asStateFlow()

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    private val _loyalty = MutableStateFlow<LoyaltyResponse?>(null)
    val loyalty: StateFlow<LoyaltyResponse?> = _loyalty.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _reviewSent = MutableStateFlow(false)
    val reviewSent: StateFlow<Boolean> = _reviewSent.asStateFlow()

    private var trackingJob: Job? = null

    // ── Suivi d'une commande (Polling en temps réel J3) ──────────────────────
    fun trackOrder(orderId: Long) {
        stopTracking()
        trackingJob = viewModelScope.launch {
            while (true) {
                try {
                    val order = apiService.trackOrder(orderId)
                    _trackedOrder.value = order
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(4000) // Rafraîchit l'état toutes les 4 secondes
            }
        }
    }

    fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }

    // ── Création de commande ─────────────────────────────────────────────────
    fun checkout(tableNumber: String?, items: List<CartItem>, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val cartItemRequests = items.map { 
                    CartItemRequest(dish_id = it.dish.id, quantity = it.quantity, comment = it.comment) 
                }
                val request = CheckoutRequest(table_number = tableNumber, items = cartItemRequests)
                val response = apiService.createOrder(request)
                onResult(response.id)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Historique des commandes ─────────────────────────────────────────────
    fun loadOrderHistory() {
        viewModelScope.launch {
            try {
                val history = apiService.getOrderHistory()
                _orderHistory.value = history
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ── J3 : Fidélité ────────────────────────────────────────────────────────
    fun loadLoyalty() {
        viewModelScope.launch {
            try {
                val response = apiService.getLoyalty()
                _loyalty.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ── J3 : Stripe Intent ───────────────────────────────────────────────────
    fun createPaymentIntent(orderId: Long, convives: Int, onResponse: (PaymentIntentResponse?) -> Unit) {
        viewModelScope.launch {
            try {
                val request = PaymentIntentRequest(orderId = orderId, convives = convives)
                val response = apiService.createPaymentIntent(request)
                onResponse(response)
            } catch (e: Exception) {
                e.printStackTrace()
                onResponse(null)
            }
        }
    }

    // ── J3 : Notation / Review ───────────────────────────────────────────────
    fun submitReview(orderId: Long, rating: Int, comment: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val request = ReviewRequest(orderId = orderId, rating = rating, comment = comment)
                val response = apiService.submitReview(orderId, request)
                if (response.success) {
                    _reviewSent.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetReviewSent() {
        _reviewSent.value = false
    }

    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }
}
