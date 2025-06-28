package com.rotafacil.app.domain.repository

import com.rotafacil.app.domain.model.Route
import kotlinx.coroutines.flow.Flow

interface RouteRepository {
    suspend fun getActiveRoutes(): Result<List<Route>>
    suspend fun getRouteById(id: String): Result<Route>
    suspend fun getRoutesByDriver(driverId: String): Result<List<Route>>
    suspend fun saveRoutesToCache(routes: List<Route>)
    suspend fun getRoutesFromCache(): List<Route>
    fun observeActiveRoutes(): Flow<List<Route>>
    suspend fun searchRoutesByText(text: String): Result<List<Route>>
    suspend fun filterRoutes(turno: String?, ativa: Boolean?): Result<List<Route>>
    suspend fun createRoute(route: Route): Result<Route>
    suspend fun updateRoute(route: Route): Result<Route>
    suspend fun deleteRoute(routeId: String): Result<Unit>
} 