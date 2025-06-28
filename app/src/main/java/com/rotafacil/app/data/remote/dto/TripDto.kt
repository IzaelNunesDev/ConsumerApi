package com.rotafacil.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TripDto(
    val id: String,
    @Json(name = "rota_id")
    val rotaId: String,
    @Json(name = "motorista_id")
    val motoristaId: String,
    @Json(name = "veiculo_id")
    val veiculoId: String,
    @Json(name = "data_viagem")
    val dataViagem: String,
    val status: String,
    val incidentes: List<IncidentDto> = emptyList()
)

@JsonClass(generateAdapter = true)
data class StudentDto(
    val id: String,
    val nome: String,
    val email: String,
    val telefone: String? = null
)

@JsonClass(generateAdapter = true)
data class IncidentDto(
    val id: String,
    val descricao: String,
    val tipo: String,
    @Json(name = "data_hora")
    val timestamp: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@JsonClass(generateAdapter = true)
data class AttendanceDto(
    val id: String,
    @Json(name = "aluno_id")
    val alunoId: String,
    @Json(name = "viagem_id")
    val viagemId: String,
    @Json(name = "data_hora_embarque")
    val timestamp: String,
    @Json(name = "tipo_registro")
    val tipoRegistro: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@JsonClass(generateAdapter = true)
data class IncidentRequestDto(
    val descricao: String,
    val tipo: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@JsonClass(generateAdapter = true)
data class AttendanceRequestDto(
    @Json(name = "aluno_id")
    val alunoId: String,
    @Json(name = "viagem_id")
    val viagemId: String,
    @Json(name = "tipo_registro")
    val tipoRegistro: String,
    val latitude: Double? = null,
    val longitude: Double? = null
) 