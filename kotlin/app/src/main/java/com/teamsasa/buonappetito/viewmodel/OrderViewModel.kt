package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamsasa.buonappetito.data.model.*
import com.teamsasa.buonappetito.data.repository.OrderRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

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
                repository.trackOrder(orderId).onSuccess { order ->
                    _trackedOrder.value = order
                }.onFailure { e ->
                    e.printStackTrace()
                }
                delay(4000) // Rafraîchit l'état toutes les 4 secondes
            }
        }
    }

    fun stopTracking(clearState: Boolean = true) {
        trackingJob?.cancel()
        trackingJob = null
        if (clearState) {
            _trackedOrder.value = null
        }
    }

    // ── Création de commande ─────────────────────────────────────────────────
    fun checkout(request: CheckoutRequest, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.createOrder(request).onSuccess { response ->
                onResult(response.id)
            }.onFailure { e ->
                e.printStackTrace()
            }
            _isLoading.value = false
        }
    }

    // ── Historique des commandes ─────────────────────────────────────────────
    fun loadOrderHistory() {
        viewModelScope.launch {
            repository.getOrderHistory().onSuccess { history ->
                _orderHistory.value = history
            }.onFailure { e ->
                e.printStackTrace()
            }
        }
    }

    // ── J3 : Fidélité ────────────────────────────────────────────────────────
    fun loadLoyalty() {
        viewModelScope.launch {
            repository.getLoyalty().onSuccess { response ->
                _loyalty.value = response
            }.onFailure { e ->
                e.printStackTrace()
            }
        }
    }

    // ── J3 : Stripe Intent ───────────────────────────────────────────────────
    fun createPaymentIntent(orderId: Long, convives: Int, onResponse: (PaymentIntentResponse?) -> Unit) {
        viewModelScope.launch {
            repository.createPaymentIntent(orderId, convives).onSuccess { response ->
                onResponse(response)
            }.onFailure { e ->
                e.printStackTrace()
                onResponse(null)
            }
        }
    }

    // ── J3 : Notation / Review ───────────────────────────────────────────────
    fun submitReview(orderId: Long, rating: Int, comment: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.submitReview(orderId, rating, comment).onSuccess { response ->
                if (response.success) {
                    _reviewSent.value = true
                }
            }.onFailure { e ->
                e.printStackTrace()
            }
            _isLoading.value = false
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
