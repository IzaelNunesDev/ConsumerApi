package com.rotafacil.app.domain.usecase

import com.rotafacil.app.data.remote.dto.LoginResponseDto
import com.rotafacil.app.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponseDto> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email e senha são obrigatórios"))
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(IllegalArgumentException("Email inválido"))
        }
        
        return authRepository.login(email, password)
    }
} 