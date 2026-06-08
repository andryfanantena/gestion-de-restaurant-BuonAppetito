package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamsasa.buonappetito.data.model.CartItemRequest
import com.teamsasa.buonappetito.data.model.Order
import com.teamsasa.buonappetito.data.repository.OrderRepository
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    private val _orderHistory = MutableStateFlow<List<Order>>(emptyList())
    val orderHistory: StateFlow<List<Order>> = _orderHistory.asStateFlow()

    private val _trackedOrder = MutableStateFlow<Order?>(null)
    val trackedOrder: StateFlow<Order?> = _trackedOrder.asStateFlow()

    private var isTracking = false

    fun loadOrderHistory(token: String) {
        viewModelScope.launch {
            repository.getOrderHistory(token).onSuccess { history ->
                _orderHistory.value = history
            }
        }
    }

    fun checkout(itemsRequest: List<CartItemRequest>, onCheckoutSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            repository.createOrder(itemsRequest).onSuccess { order ->
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
