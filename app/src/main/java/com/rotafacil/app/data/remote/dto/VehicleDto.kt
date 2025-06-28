package com.rotafacil.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleDto(
    val id: String,
    val placa: String,
    val modelo: String,
    @Json(name = "capacidade_passageiros")
    val capacidade: Int,
    @Json(name = "ano_fabricacao")
    val ano: Int,
    @Json(name = "status_manutencao")
    val statusManutencao: String = "Disponível",
    @Json(name = "adaptado_pcd")
    val adaptadoPcd: Boolean = false,
    @Json(name = "motorista_id")
    val motoristaId: String? = null
) 