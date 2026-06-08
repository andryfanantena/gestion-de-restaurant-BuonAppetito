package com.teamsasa.buonappetito.data.model


data class User(
    val id: Long,
    val name: String,
    val email: String,
    val token: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val user: User?,
    val token: String?
)