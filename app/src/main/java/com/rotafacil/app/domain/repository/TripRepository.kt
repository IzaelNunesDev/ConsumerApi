package com.rotafacil.app.domain.repository

import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.model.TripLocation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TripRepository {
    suspend fun getTripsByStudent(studentId: String): Result<List<Trip>>
    suspend fun getTripsByDriver(driverId: String, date: LocalDate? = null): Result<List<Trip>>
    suspend fun getTripById(id: String): Result<Trip>
    suspend fun updateTripStatus(tripId: String, status: String): Result<Trip>
    suspend fun reportIncident(tripId: String, description: String, type: String): Result<Unit>
    suspend fun markAttendance(tripId: String, studentId: String, present: Boolean): Result<Unit>
    suspend fun saveTripsToCache(trips: List<Trip>)
    suspend fun getTripsFromCache(): List<Trip>
    fun observeTripLocation(tripId: String): Flow<TripLocation>
    suspend fun sendLocationUpdate(tripId: String, latitude: Double, longitude: Double)
    suspend fun filterTrips(status: String?, dataInicio: String?, dataFim: String?): Result<List<Trip>>
    suspend fun searchTripsByText(texto: String): Result<List<Trip>>
} 