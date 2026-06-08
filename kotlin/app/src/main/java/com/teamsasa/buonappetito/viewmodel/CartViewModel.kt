package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import com.teamsasa.buonappetito.data.model.CartItem
import com.teamsasa.buonappetito.data.model.Dish
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    fun addToCart(dish: Dish, quantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val existingItem = currentList.find { it.dish.id == dish.id }

        if (existingItem != null) {
            existingItem.quantity += quantity
        } else {
            currentList.add(CartItem(dish, quantity))
        }
        _cartItems.value = currentList
        calculateTotal()
    }

    fun updateQuantity(dishId: Long, newQuantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val item = currentList.find { it.dish.id == dishId }

        if (item != null) {
            if (newQuantity <= 0) {
                currentList.remove(item)
            } else {
                item.quantity = newQuantity
            }
            _cartItems.value = currentList
            calculateTotal()
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _totalPrice.value = 0.0
    }

    private fun calculateTotal() {
        _totalPrice.value = _cartItems.value.sumOf { it.dish.price * it.quantity }
    }
}
