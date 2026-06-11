package com.teamsasa.buonappetito.data.api

import com.teamsasa.buonappetito.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        
        // Ajout systématique du header Accept pour Laravel
        requestBuilder.addHeader("Accept", "application/json")

        // Si un token existe, on l'ajoute automatiquement
        sessionManager.fetchAuthToken()?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
