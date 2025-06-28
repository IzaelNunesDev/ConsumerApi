package com.rotafacil.app.domain.usecase

import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.repository.RouteRepository
import javax.inject.Inject

class CreateRouteUseCase @Inject constructor(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(route: Route): Result<Route> {
        return routeRepository.createRoute(route)
    }
} 