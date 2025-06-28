package com.rotafacil.app.domain.model

data class Route(
    val id: String,
    val nome: String,
    val turno: String,
    val pontosDeParada: List<StopPoint>,
    val isAtiva: Boolean = true,
    val motoristaId: String? = null,
    val veiculoId: String? = null
)

data class StopPoint(
    val id: String,
    val nome: String,
    val latitude: Double,
    val longitude: Double,
    val ordem: Int,
    val horarioEstimado: String? = null
) 