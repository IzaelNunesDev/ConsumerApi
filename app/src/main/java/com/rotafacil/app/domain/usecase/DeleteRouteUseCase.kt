package com.rotafacil.app.domain.usecase

import com.rotafacil.app.domain.repository.RouteRepository
import javax.inject.Inject

class DeleteRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeId: String): Result<Unit> {
        return routeRepository.deleteRoute(routeId)
    }
} 