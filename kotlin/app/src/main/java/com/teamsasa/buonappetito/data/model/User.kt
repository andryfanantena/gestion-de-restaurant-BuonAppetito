package com.teamsasa.buonappetito.data.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val role: String = "client",
    val token: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val role: String = "client"
)

data class AuthResponse(
    val success: Boolean,
    val message: String?,
    val user: User?,
    val token: String?
)

data class LogoutResponse(
    val success: Boolean,
    val message: String?
)
