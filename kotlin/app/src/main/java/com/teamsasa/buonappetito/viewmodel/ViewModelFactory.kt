package com.teamsasa.buonappetito.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teamsasa.buonappetito.data.api.ApiClient
import com.teamsasa.buonappetito.data.api.AuthInterceptor
import com.teamsasa.buonappetito.data.local.SessionManager
import com.teamsasa.buonappetito.data.repository.AuthRepository
import com.teamsasa.buonappetito.data.repository.MenuRepository
import com.teamsasa.buonappetito.data.repository.OrderRepository

class ViewModelFactory(private val sessionManager: SessionManager) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val authInterceptor = AuthInterceptor(sessionManager)
        val apiService = ApiClient.getApiService(authInterceptor)
        
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(AuthRepository(apiService), sessionManager) as T
            }
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> {
                MenuViewModel(MenuRepository(apiService)) as T
            }
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                OrderViewModel(OrderRepository(apiService)) as T
            }
            modelClass.isAssignableFrom(CartViewModel::class.java) -> {
                CartViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
