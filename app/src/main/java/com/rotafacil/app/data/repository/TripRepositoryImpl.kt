package com.rotafacil.app.data.repository

import com.rotafacil.app.data.remote.ApiService
import com.rotafacil.app.data.remote.mapper.toTrip
import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.model.TripLocation
import com.rotafacil.app.domain.repository.TripRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : TripRepository {
    
    override suspend fun getTripsByStudent(studentId: String): Result<List<Trip>> {
        return try {
            val trips = apiService.getTripsByStudent(studentId).map { it.toTrip() }
            Result.success(trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTripsByDriver(driverId: String, date: LocalDate?): Result<List<Trip>> {
        return try {
            val startDate = date?.toString()
            val endDate = date?.toString()
            val trips = apiService.getTripsByDriver(driverId, startDate, endDate).map { it.toTrip() }
            Result.success(trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getTripById(id: String): Result<Trip> {
        return try {
            val response = apiService.getTripById(id)
            val trip = response.toTrip()
            Result.success(trip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateTripStatus(tripId: String, status: String): Result<Trip> {
        return try {
            val trip = apiService.updateTripStatus(tripId, mapOf("status" to status)).toTrip()
            Result.success(trip)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun reportIncident(tripId: String, description: String, type: String): Result<Unit> {
        return try {
            // TODO: Implementar quando o DTO estiver disponível
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun markAttendance(tripId: String, studentId: String, present: Boolean): Result<Unit> {
        return try {
            // TODO: Implementar quando o DTO estiver disponível
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveTripsToCache(trips: List<Trip>) {
        // TODO: Implementar cache local com Room
    }
    
    override suspend fun getTripsFromCache(): List<Trip> {
        // TODO: Implementar cache local com Room
        return emptyList()
    }
    
    override fun observeTripLocation(tripId: String): Flow<TripLocation> = flow {
        // TODO: Implementar observação de localização em tempo real
    }
    
    override suspend fun sendLocationUpdate(tripId: String, latitude: Double, longitude: Double) {
        // TODO: Implementar envio de localização
    }
    
    override suspend fun filterTrips(status: String?, dataInicio: String?, dataFim: String?): Result<List<Trip>> {
        return try {
            val response = apiService.filterTrips(status, dataInicio, dataFim)
            val trips = response.map { it.toTrip() }
            Result.success(trips)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 