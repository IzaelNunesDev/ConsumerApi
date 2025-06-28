package com.rotafacil.app.data.remote.mapper

import com.rotafacil.app.data.remote.dto.RouteDto
import com.rotafacil.app.data.remote.dto.StopPointDto
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.model.StopPoint

fun RouteDto.toDomain(): Route {
    return Route(
        id = id,
        nome = nome,
        turno = turno,
        pontosDeParada = pontosDeParada.map { it.toDomain() },
        isAtiva = ativa,
        motoristaId = null,
        veiculoId = null
    )
}

fun StopPointDto.toDomain(): StopPoint {
    return StopPoint(
        id = id,
        nome = nome,
        latitude = lat,
        longitude = lon,
        ordem = ordem,
        horarioEstimado = null
    )
}

fun Route.toDto(): RouteDto {
    return RouteDto(
        id = id,
        nome = nome,
        descricao = "",
        turno = turno,
        pontosDeParada = pontosDeParada.map { it.toDto() },
        ativa = isAtiva
    )
}

fun StopPoint.toDto(): StopPointDto {
    return StopPointDto(
        id = id,
        nome = nome,
        endereco = "",
        lat = latitude,
        lon = longitude,
        ordem = ordem
    )
} 