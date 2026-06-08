package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamsasa.buonappetito.data.model.CheckoutRequest
import com.teamsasa.buonappetito.data.model.Order
import com.teamsasa.buonappetito.data.repository.OrderRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    private val _trackedOrder = MutableStateFlow<Order?>(null)
    val trackedOrder: StateFlow<Order?> = _trackedOrder.asStateFlow()

    private var isTracking = false

    fun loadOrderHistory() {
        viewModelScope.launch {
            repository.getOrderHistory().onSuccess { history ->
                _orderHistory.value = history
            }
        }
    }

    fun checkout(request: CheckoutRequest, onCheckoutSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            repository.createOrder(request).onSuccess { order ->
                _trackedOrder.value = order
                onCheckoutSuccess(order.id)
            }
        }
    }

    fun startTrackingOrder(orderId: Long) {
        isTracking = true
        viewModelScope.launch {
            while (isTracking) {
                repository.trackOrder(orderId).onSuccess { order ->
                    _trackedOrder.value = order
                    if (order.status == "DELIVERED") {
                        isTracking = false
                    }
                }
                delay(5000)
            }
        }
    }

    fun stopTrackingOrder() {
        isTracking = false
    }

    override fun onCleared() {
        super.onCleared()
        stopTrackingOrder()
    }
}
