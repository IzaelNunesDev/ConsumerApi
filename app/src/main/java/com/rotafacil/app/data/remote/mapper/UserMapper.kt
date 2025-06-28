package com.rotafacil.app.data.remote.mapper

import com.rotafacil.app.data.remote.dto.UserDto
import com.rotafacil.app.domain.model.User
import com.rotafacil.app.domain.model.UserRole

fun UserDto.toDomain(): User {
    return User(
        id = id,
        nome = nome,
        email = email,
        role = when (tipo.lowercase()) {
            "aluno" -> UserRole.ALUNO
            "motorista" -> UserRole.MOTORISTA
            "admin" -> UserRole.ADMIN
            else -> UserRole.ALUNO
        },
        telefone = telefone,
        endereco = endereco,
        matricula = registration
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        nome = nome,
        email = email,
        tipo = when (role) {
            UserRole.ALUNO -> "aluno"
            UserRole.MOTORISTA -> "motorista"
            UserRole.ADMIN -> "admin"
        },
        telefone = telefone,
        endereco = endereco,
        registration = matricula
    )
} 