package com.teamsasa.buonappetito.data.model

data class CartItem(
    val dish: Dish,
    val quantity: Int,
    val comment: String = ""
)

data class CartItemRequest(
    val dish_id: Long,
    val quantity: Int,
    val comment: String? = null
)

data class CheckoutRequest(
    val table_number: String?,
    val items: List<CartItemRequest>
)
