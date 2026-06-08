package com.teamsasa.buonappetito.data.model


data class CartItem(
    val dish: Dish,
    var quantity: Int
)

data class CartItemRequest(
    val dish_id: Long,
    val quantity: Int
)