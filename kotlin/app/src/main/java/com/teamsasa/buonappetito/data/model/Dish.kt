package com.teamsasa.buonappetito.data.model

import com.google.gson.annotations.SerializedName

data class Dish(
    val id: Long,
    val name: String,
    val description: String,
    val price: Double,
    @SerializedName("image_url")
    val imageUrl: String = "",
    val category: String,
    val rating: Double = 0.0,
    @SerializedName("prepartion_time")
    val preparationTime: String = "15-20 min",
    @SerializedName("is_available")
    val isAvailable: Boolean = true
)