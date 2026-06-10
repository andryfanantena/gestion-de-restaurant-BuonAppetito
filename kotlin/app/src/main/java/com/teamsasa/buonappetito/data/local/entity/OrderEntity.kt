package com.teamsasa.buonappetito.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val id: Long,
    val orderNumber: String,
    val totalPrice: Double,
    val status: String,
    val date: String,
    val tableNumber: String?
)