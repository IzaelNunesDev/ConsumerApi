package com.rotafacil.app.data.remote.mapper

import com.rotafacil.app.data.remote.dto.TripDto
import com.rotafacil.app.data.remote.dto.StudentDto
import com.rotafacil.app.data.remote.dto.IncidentDto
import com.rotafacil.app.data.remote.dto.AttendanceDto
import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.model.TripStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun TripDto.toTrip(): Trip {
    return Trip(
        id = id,
        rotaId = rotaId,
        motoristaId = motoristaId,
        veiculoId = veiculoId,
        dataInicio = LocalDateTime.parse(dataViagem),
        dataFim = null, // Não há dataFim no DTO
        status = when (status.lowercase()) {
            "agendada" -> TripStatus.AGENDADA
            "em_andamento" -> TripStatus.EM_ANDAMENTO
            "concluida" -> TripStatus.CONCLUIDA
            "cancelada" -> TripStatus.CANCELADA
            else -> TripStatus.AGENDADA
        },
        alunos = emptyList(), // Não há alunos no DTO
        incidentes = incidentes.map { it.toIncident() },
        frequencias = emptyList() // Não há frequencias no DTO
    )
}

fun StudentDto.toStudent(): com.rotafacil.app.domain.model.Student {
    return com.rotafacil.app.domain.model.Student(
        id = id,
        nome = nome,
        email = email,
        telefone = telefone
    )
}

fun IncidentDto.toIncident(): com.rotafacil.app.domain.model.Incident {
    return com.rotafacil.app.domain.model.Incident(
        id = id,
        descricao = descricao,
        tipo = when (tipo.lowercase()) {
            "atraso" -> com.rotafacil.app.domain.model.IncidentType.ATRASO
            "acidente" -> com.rotafacil.app.domain.model.IncidentType.ACIDENTE
            "problema_mecanico" -> com.rotafacil.app.domain.model.IncidentType.PROBLEMA_MECANICO
            else -> com.rotafacil.app.domain.model.IncidentType.OUTRO
        },
        timestamp = LocalDateTime.parse(timestamp),
        latitude = latitude,
        longitude = longitude
    )
}

fun AttendanceDto.toAttendance(): com.rotafacil.app.domain.model.Attendance {
    return com.rotafacil.app.domain.model.Attendance(
        id = id,
        alunoId = alunoId,
        viagemId = viagemId,
        presente = tipoRegistro.lowercase() == "embarque",
        timestamp = LocalDateTime.parse(timestamp),
        latitude = latitude,
        longitude = longitude
    )
} 