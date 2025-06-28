package com.rotafacil.app.domain.repository

import com.rotafacil.app.data.remote.dto.TripStatsDto
import com.rotafacil.app.data.remote.dto.VehicleStatsDto
import com.rotafacil.app.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    suspend fun getVehicles(): Result<List<Vehicle>>
    suspend fun getVehicleById(id: String): Result<Vehicle>
    suspend fun getVehiclesByDriver(driverId: String): Result<List<Vehicle>>
    suspend fun saveVehiclesToCache(vehicles: List<Vehicle>)
    suspend fun getVehiclesFromCache(): List<Vehicle>
    fun observeVehicles(): Flow<List<Vehicle>>
    suspend fun searchVehiclesByText(texto: String): Result<List<Vehicle>>
    suspend fun filterVehicles(statusManutencao: String?, adaptadoPcd: Boolean?): Result<List<Vehicle>>
    suspend fun getVehicleStats(): Result<List<VehicleStatsDto>>
    suspend fun getTripStatsByPeriod(dataInicio: String? = null, dataFim: String? = null): Result<TripStatsDto>
} 