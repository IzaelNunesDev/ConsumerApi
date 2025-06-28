package com.rotafacil.app.domain.model

data class Vehicle(
    val id: String,
    val placa: String,
    val modelo: String,
    val capacidade: Int,
    val ano: Int,
    val isAtivo: Boolean = true,
    val motoristaId: String? = null,
    val statusManutencao: String = "Disponível",
    val adaptadoPcd: Boolean = false
) 