package com.teamsasa.buonappetito.data.model


data class Dish(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    val imageUrl: String,
    val category: String,
    val rating: Double = 4.8,
    val preparationTime: String = "15-20 min"
)