package com.rotafacil.app.domain.usecase

import com.rotafacil.app.domain.model.Vehicle
import com.rotafacil.app.domain.repository.VehicleRepository
import javax.inject.Inject

class GetVehiclesUseCase @Inject constructor(
    private val vehicleRepository: VehicleRepository
) {
    suspend operator fun invoke(): Result<List<Vehicle>> {
        return vehicleRepository.getVehicles()
    }

    suspend fun search(text: String): Result<List<Vehicle>> {
        return vehicleRepository.searchVehiclesByText(text)
    }

    suspend fun filter(statusManutencao: String?, adaptadoPcd: Boolean?): Result<List<Vehicle>> {
        return vehicleRepository.filterVehicles(statusManutencao, adaptadoPcd)
    }
} 