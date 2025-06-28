package com.rotafacil.app.data.repository

import com.rotafacil.app.data.local.DataStoreManager
import com.rotafacil.app.data.remote.ApiService
import com.rotafacil.app.data.remote.dto.LoginRequestDto
import com.rotafacil.app.data.remote.dto.LoginResponseDto
import com.rotafacil.app.domain.model.User
import com.rotafacil.app.domain.model.UserRole
import com.rotafacil.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponseDto> {
        return try {
            val loginRequest = LoginRequestDto(email = email, senha = password)
            val response = apiService.login(loginRequest)
            
            // Salvar dados de autenticação
            dataStoreManager.saveAuthToken(response.accessToken)
            dataStoreManager.saveUserData(
                userId = response.userInfo.id,
                email = response.userInfo.email,
                name = response.userInfo.nome,
                role = response.userInfo.tipo
            )
            
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerStudent(
        name: String,
        email: String,
        password: String,
        registration: String,
        phone: String?
    ): Result<LoginResponseDto> {
        return try {
            val studentData = mapOf(
                "nome_completo" to name,
                "email" to email,
                "senha" to password,
                "matricula" to registration,
                "telefone" to (phone ?: "")
            )
            val response = apiService.registerStudent(studentData)
            
            // Salvar dados de autenticação
            dataStoreManager.saveAuthToken(response.accessToken)
            dataStoreManager.saveUserData(
                userId = response.userInfo.id,
                email = response.userInfo.email,
                name = response.userInfo.nome,
                role = response.userInfo.tipo
            )
            
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        dataStoreManager.logout()
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val userId = dataStoreManager.userId.firstOrNull()
            val userEmail = dataStoreManager.userEmail.firstOrNull()
            val userName = dataStoreManager.userName.firstOrNull()
            val userRole = dataStoreManager.userRole.firstOrNull()
            
            if (userId != null && userEmail != null && userName != null && userRole != null) {
                val user = User(
                    id = userId,
                    nome = userName,
                    email = userEmail,
                    role = when (userRole.lowercase()) {
                        "aluno" -> UserRole.ALUNO
                        "motorista" -> UserRole.MOTORISTA
                        "admin" -> UserRole.ADMIN
                        else -> UserRole.ALUNO
                    }
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Usuário não encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveAuthToken(token: String) {
        dataStoreManager.saveAuthToken(token)
    }

    override suspend fun getAuthToken(): String? {
        return dataStoreManager.getAuthToken()
    }

    override suspend fun clearAuthToken() {
        dataStoreManager.clearAuthToken()
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return dataStoreManager.authToken.map { token ->
            token != null
        }
    }
} 