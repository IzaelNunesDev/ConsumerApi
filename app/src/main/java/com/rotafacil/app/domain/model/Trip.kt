package com.rotafacil.app.domain.model

import java.time.LocalDateTime

data class Trip(
    val id: String,
    val rotaId: String,
    val motoristaId: String,
    val veiculoId: String,
    val dataInicio: LocalDateTime,
    val dataFim: LocalDateTime? = null,
    val status: TripStatus,
    val alunos: List<Student> = emptyList(),
    val incidentes: List<Incident> = emptyList(),
    val frequencias: List<Attendance> = emptyList()
)

enum class TripStatus {
    AGENDADA,
    EM_ANDAMENTO,
    CONCLUIDA,
    CANCELADA
}

data class Student(
    val id: String,
    val nome: String,
    val email: String,
    val telefone: String? = null
)

data class Incident(
    val id: String,
    val descricao: String,
    val tipo: IncidentType,
    val timestamp: LocalDateTime,
    val latitude: Double? = null,
    val longitude: Double? = null
)

enum class IncidentType {
    ATRASO,
    ACIDENTE,
    PROBLEMA_MECANICO,
    OUTRO
}

data class Attendance(
    val id: String,
    val alunoId: String,
    val viagemId: String,
    val presente: Boolean,
    val timestamp: LocalDateTime,
    val latitude: Double? = null,
    val longitude: Double? = null
) 