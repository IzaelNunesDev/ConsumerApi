package com.rotafacil.app.data.remote.mapper

import com.rotafacil.app.data.remote.dto.VehicleDto
import com.rotafacil.app.domain.model.Vehicle

fun VehicleDto.toVehicle(): Vehicle {
    return Vehicle(
        id = id,
        placa = placa,
        modelo = modelo,
        capacidade = capacidade,
        ano = ano,
        isAtivo = true, // Assumindo que se está na API, está ativo
        motoristaId = motoristaId,
        statusManutencao = statusManutencao,
        adaptadoPcd = adaptadoPcd
    )
}

fun Vehicle.toVehicleDto(): VehicleDto {
    return VehicleDto(
        id = id,
        placa = placa,
        modelo = modelo,
        capacidade = capacidade,
        ano = ano,
        statusManutencao = statusManutencao,
        adaptadoPcd = adaptadoPcd,
        motoristaId = motoristaId
    )
} 