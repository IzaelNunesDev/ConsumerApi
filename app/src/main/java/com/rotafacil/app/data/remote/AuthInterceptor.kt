package com.rotafacil.app.data.remote

import com.rotafacil.app.data.local.DataStoreManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        
        // Obter token de forma síncrona (não recomendado, mas necessário para interceptor)
        val token = runBlocking { dataStoreManager.getAuthToken() }
        
        val request = original.newBuilder()
            .header("Content-Type", "application/json")
            .apply {
                if (token != null) {
                    header("Authorization", "Bearer $token")
                }
            }
            .method(original.method, original.body)
            .build()
        
        return chain.proceed(request)
    }
} 