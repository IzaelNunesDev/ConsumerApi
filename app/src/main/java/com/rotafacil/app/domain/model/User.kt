package com.rotafacil.app.domain.model

data class User(
    val id: String,
    val nome: String,
    val email: String,
    val role: UserRole,
    val telefone: String? = null,
    val endereco: String? = null
)

enum class UserRole {
    ALUNO,
    MOTORISTA,
    ADMIN
} 