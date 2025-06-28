package com.rotafacil.app.data.repository

import com.rotafacil.app.data.remote.ApiService
import com.rotafacil.app.data.remote.mapper.toVehicle
import com.rotafacil.app.domain.model.Vehicle
import com.rotafacil.app.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class VehicleRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : VehicleRepository {
    
    override suspend fun getVehicles(): Result<List<Vehicle>> {
        return try {
            val vehicles = apiService.getVehicles().map { it.toVehicle() }
            Result.success(vehicles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getVehicleById(id: String): Result<Vehicle> {
        return try {
            val vehicle = apiService.getVehicle(id).toVehicle()
            Result.success(vehicle)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getVehiclesByDriver(driverId: String): Result<List<Vehicle>> {
        return try {
            // TODO: Implementar quando o endpoint estiver disponível
            val vehicles = apiService.getVehicles().filter { it.motoristaId == driverId }.map { it.toVehicle() }
            Result.success(vehicles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveVehiclesToCache(vehicles: List<Vehicle>) {
        // TODO: Implementar cache local com Room
    }
    
    override suspend fun getVehiclesFromCache(): List<Vehicle> {
        // TODO: Implementar cache local com Room
        return emptyList()
    }
    
    override fun observeVehicles(): Flow<List<Vehicle>> = flow {
        val vehicles = getVehicles().getOrNull() ?: emptyList()
        emit(vehicles)
    }
    
    override suspend fun searchVehiclesByText(texto: String): Result<List<Vehicle>> {
        return try {
            val response = apiService.searchVehiclesByText(texto)
            val vehicles = response.map { it.toVehicle() }
            Result.success(vehicles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun filterVehicles(statusManutencao: String?, adaptadoPcd: Boolean?): Result<List<Vehicle>> {
        return try {
            val response = apiService.filterVehicles(statusManutencao, adaptadoPcd)
            val vehicles = response.map { it.toVehicle() }
            Result.success(vehicles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 