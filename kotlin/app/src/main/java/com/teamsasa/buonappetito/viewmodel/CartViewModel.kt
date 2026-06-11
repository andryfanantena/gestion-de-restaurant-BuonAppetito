package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import com.teamsasa.buonappetito.data.model.CartItem
import com.teamsasa.buonappetito.data.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow<Double>(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _tableNumber = MutableStateFlow<String?>(null)
    val tableNumber: StateFlow<String?> = _tableNumber.asStateFlow()

    fun setTableNumber(number: String) {
        _tableNumber.value = number
    }

    fun addToCart(dish: Dish, quantity: Int, comment: String = "") {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.dish.id == dish.id && it.comment == comment }

        if (index != -1) {
            val existingItem = currentList[index]
            currentList[index] = existingItem.copy(quantity = existingItem.quantity + quantity)
        } else {
            currentList.add(CartItem(dish, quantity, comment))
        }
        _cartItems.value = currentList
        calculateTotal()
    }

    fun updateComment(dishId: Long, oldComment: String, newComment: String) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.dish.id == dishId && it.comment == oldComment }
        
        if (index != -1) {
            // Check if there's already an item with the same dishId and the NEW comment
            val mergeIndex = currentList.indexOfFirst { it.dish.id == dishId && it.comment == newComment }
            
            if (mergeIndex != -1 && mergeIndex != index) {
                // Merge quantities
                val targetItem = currentList[mergeIndex]
                val sourceItem = currentList[index]
                currentList[mergeIndex] = targetItem.copy(quantity = targetItem.quantity + sourceItem.quantity)
                currentList.removeAt(index)
            } else {
                currentList[index] = currentList[index].copy(comment = newComment)
            }
            _cartItems.value = currentList
        }
    }

    fun updateQuantity(dishId: Long, comment: String, newQuantity: Int) {
        val currentList = _cartItems.value.toMutableList()
        val index = currentList.indexOfFirst { it.dish.id == dishId && it.comment == comment }

        if (index != -1) {
            if (newQuantity <= 0) {
                currentList.removeAt(index)
            } else {
                val item = currentList[index]
                currentList[index] = item.copy(quantity = newQuantity)
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
        val total = _cartItems.value.sumOf { it.dish.price * it.quantity.toDouble() }
        _totalPrice.value = total
    }
}
