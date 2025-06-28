package com.rotafacil.app.domain.usecase

import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveRoutesUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(): Result<List<Route>> {
        return routeRepository.getActiveRoutes()
    }
    
    fun observe(): Flow<List<Route>> {
        return routeRepository.observeActiveRoutes()
    }

    suspend fun search(text: String): Result<List<Route>> {
        return routeRepository.searchRoutesByText(text)
    }

    suspend fun filter(turno: String?, ativa: Boolean?): Result<List<Route>> {
        return routeRepository.filterRoutes(turno, ativa)
    }
} 