package com.teamsasa.buonappetito.data.api

import com.teamsasa.buonappetito.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private fun createOkHttpClient(interceptor: AuthInterceptor?): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
        
        interceptor?.let {
            builder.addInterceptor(it)
        }
        
        return builder.build()
    }

    fun getApiService(interceptor: AuthInterceptor? = null): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(createOkHttpClient(interceptor))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
