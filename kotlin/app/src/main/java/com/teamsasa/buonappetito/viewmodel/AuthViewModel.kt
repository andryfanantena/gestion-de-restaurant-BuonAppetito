package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamsasa.buonappetito.data.model.LoginRequest
import com.teamsasa.buonappetito.data.model.RegisterRequest
import com.teamsasa.buonappetito.data.model.User
import com.teamsasa.buonappetito.data.repository.AuthRepository
import kotlinx.coroutines.launch
import kotlinx.flow.MutableStateFlow
import kotlinx.flow.StateFlow
import kotlinx.flow.asStateFlow

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.login(LoginRequest(email, password))
            _isLoading.value = false
            result.onSuccess { response ->
                if (response.success && response.user != null) {
                    _currentUser.value = response.user
                    _token.value = response.token
                    onLoginSuccess()
                } else {
                    _error.value = response.message ?: "Erreur de connexion"
                }
            }.onFailure {
                _error.value = it.message ?: "Une erreur est survenue"
            }
        }
    }

    fun register(name: String, email: String, password: String, onRegisterSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.register(RegisterRequest(name, email, password))
            _isLoading.value = false
            result.onSuccess { response ->
                if (response.success && response.user != null) {
                    _currentUser.value = response.user
                    _token.value = response.token
                    onRegisterSuccess()
                } else {
                    _error.value = response.message ?: "Erreur d'inscription"
                }
            }.onFailure {
                _error.value = it.message ?: "Une erreur est survenue"
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        _currentUser.value = null
        _token.value = null
        onLogoutSuccess()
    }
}
