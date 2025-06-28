package com.rotafacil.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VehicleStatsDto(
    val veiculoId: String,
    val placa: String,
    val modelo: String,
    @Json(name = "total_viagens")
    val totalViagens: Int,
    @Json(name = "viagens_concluidas")
    val viagensConcluidas: Int,
    @Json(name = "taxa_conclusao")
    val taxaConclusao: Double,
    @Json(name = "km_percorridos")
    val kmPercorridos: Double,
    @Json(name = "tempo_medio_viagem")
    val tempoMedioViagem: String? = null
)

@JsonClass(generateAdapter = true)
data class TripStatsDto(
    @Json(name = "total_viagens")
    val totalViagens: Int,
    @Json(name = "viagens_concluidas")
    val viagensConcluidas: Int,
    @Json(name = "viagens_canceladas")
    val viagensCanceladas: Int,
    @Json(name = "viagens_em_andamento")
    val viagensEmAndamento: Int,
    @Json(name = "taxa_conclusao")
    val taxaConclusao: Double,
    @Json(name = "total_alunos_transportados")
    val totalAlunosTransportados: Int,
    @Json(name = "km_total_percorrido")
    val kmTotalPercorrido: Double,
    @Json(name = "tempo_medio_viagem")
    val tempoMedioViagem: String? = null
) 