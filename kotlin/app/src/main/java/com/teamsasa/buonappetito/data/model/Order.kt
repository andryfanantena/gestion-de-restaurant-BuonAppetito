package com.teamsasa.buonappetito.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    val id: Long,
    @SerializedName("order_number") val orderNumber: String,
    val items: List<CartItem> = emptyList(),
    @SerializedName("total_price") val totalPrice: Double,
    val status: String,
    val date: String = "",
    @SerializedName("table_number") val tableNumber: String? = null
)

// J3 : Ajouter des plats
data class AddItemsRequest(
    val items: List<CartItemRequest>
)

// J3 : Notation
data class ReviewRequest(
    @SerializedName("order_id") val orderId: Long,
    val rating: Int,
    val comment: String = ""
)

data class ReviewResponse(
    val success: Boolean,
    val message: String?
)

// J3 : Stripe
data class PaymentIntentRequest(
    @SerializedName("order_id") val orderId: Long,
    val convives: Int = 1
)

data class PaymentIntentResponse(
    @SerializedName("client_secret") val clientSecret: String,
    val amount: Long,
    val currency: String
)

data class ConfirmPaymentRequest(
    @SerializedName("payment_intent_id") val paymentIntentId: String,
    @SerializedName("order_id") val orderId: Long
)

data class PaymentConfirmResponse(
    val success: Boolean,
    val message: String?
)

// J3 : Fidélité
data class LoyaltyResponse(
    val points: Int,
    val level: String,
    @SerializedName("next_reward") val nextReward: String,
    @SerializedName("points_to_next") val pointsToNext: Int
)

// J3 : Ticket PDF
data class TicketUrlResponse(
    val url: String
)
