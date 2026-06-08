package com.teamsasa.buonappetito.data.model


data class Order(
    val id: Long,
    val orderNumber: String,
    val items: List<CartItem>,
    val totalPrice: Double,
    val status: String, // "PENDING", "PREPARING", "READY", "DELIVERED"
    val date: String,
    val tableNumber: String? = null
)