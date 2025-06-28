package com.rotafacil.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: String,
    val nome: String,
    val email: String,
    val tipo: String, // "aluno" ou "motorista"
    val telefone: String? = null,
    val endereco: String? = null,
    @Json(name = "matricula")
    val registration: String? = null,
    val cnh: String? = null
)

@JsonClass(generateAdapter = true)
data class LoginRequestDto(
    val email: String,
    val senha: String
)

@JsonClass(generateAdapter = true)
data class LoginResponseDto(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "user_info")
    val userInfo: UserDto
) 