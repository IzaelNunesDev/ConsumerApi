package com.rotafacil.app.domain.usecase

import com.rotafacil.app.domain.model.Trip
import com.rotafacil.app.domain.repository.TripRepository
import java.time.LocalDate
import javax.inject.Inject

class GetTripsUseCase @Inject constructor(
    private val tripRepository: TripRepository
) {
    suspend fun getTripsByStudent(studentId: String): Result<List<Trip>> {
        return tripRepository.getTripsByStudent(studentId)
    }
    
    suspend fun getTripsByDriver(driverId: String, date: LocalDate? = null): Result<List<Trip>> {
        return tripRepository.getTripsByDriver(driverId, date)
    }
    
    suspend fun getTripById(id: String): Result<Trip> {
        return tripRepository.getTripById(id)
    }
} 