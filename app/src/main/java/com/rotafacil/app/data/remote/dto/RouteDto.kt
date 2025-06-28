package com.rotafacil.app.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteDto(
    val id: String,
    @Json(name = "nome_rota")
    val nome: String,
    val descricao: String,
    val turno: String,
    @Json(name = "pontos_de_parada")
    val pontosDeParada: List<StopPointDto>,
    val ativa: Boolean = true,
    val motoristaId: String,
    val veiculoId: String,
    val motorista: DriverDto?
)

@JsonClass(generateAdapter = true)
data class StopPointDto(
    val id: String,
    @Json(name = "nome_ponto")
    val nome: String,
    val endereco: String,
    val lat: Double,
    val lon: Double,
    val ordem: Int
)

fun RouteDto.toRoute(): Route {
    return Route(
        id = id,
        nome = nome,
        turno = turno,
        isAtiva = ativa,
        pontosDeParada = pontosDeParada.map { it.toStopPoint() },
        motoristaId = motoristaId,
        veiculoId = veiculoId,
        motorista = motorista?.toDriver()
    )
}

fun Route.toRouteDto(): RouteDto {
    return RouteDto(
        id = id,
        nome = nome,
        turno = turno,
        ativa = isAtiva,
        pontosDeParada = pontosDeParada.map { it.toStopPointDto() },
        motoristaId = motoristaId ?: "",
        veiculoId = veiculoId ?: "",
        motorista = motorista?.toDriverDto()
    )
}

fun RouteDto.Companion.fromRoute(route: Route): RouteDto {
    return RouteDto(
        id = route.id,
        nome = route.nome,
        turno = route.turno,
        isAtiva = route.isAtiva,
        pontosDeParada = route.pontosDeParada.map { StopPointDto.fromStopPoint(it) },
        motoristaId = route.motoristaId,
        veiculoId = route.veiculoId,
        motorista = route.motorista?.let { DriverDto.fromDriver(it) }
    )
} 