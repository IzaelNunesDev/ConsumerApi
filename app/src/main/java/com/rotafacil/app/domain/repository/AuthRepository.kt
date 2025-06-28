package com.rotafacil.app.domain.repository

import com.rotafacil.app.data.remote.dto.LoginResponseDto
import com.rotafacil.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponseDto>
    suspend fun registerStudent(
        name: String,
        email: String,
        password: String,
        registration: String,
        phone: String?
    ): Result<LoginResponseDto>
    suspend fun logout()
    suspend fun getCurrentUser(): Result<User>
    suspend fun saveAuthToken(token: String)
    suspend fun getAuthToken(): String?
    suspend fun clearAuthToken()
    fun isLoggedIn(): Flow<Boolean>
} 