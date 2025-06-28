package com.rotafacil.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteDto(
    @Json(name = "_id")
    val id: String,
    @Json(name = "nome_rota")
    val nome: String,
    val descricao: String,
    val turno: String,
    @Json(name = "pontos_de_parada")
    val pontosDeParada: List<StopPointDto>,
    val ativa: Boolean = true
)

@JsonClass(generateAdapter = true)
data class StopPointDto(
    @Json(name = "_id")
    val id: String,
    @Json(name = "nome_ponto")
    val nome: String,
    val endereco: String,
    val lat: Double,
    val lon: Double,
    val ordem: Int
) 