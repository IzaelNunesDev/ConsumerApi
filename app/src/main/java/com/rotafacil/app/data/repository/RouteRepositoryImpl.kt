package com.rotafacil.app.data.repository

import com.rotafacil.app.data.remote.ApiService
import com.rotafacil.app.data.remote.mapper.toDomain
import com.rotafacil.app.data.remote.mapper.toDto
import com.rotafacil.app.domain.model.Route
import com.rotafacil.app.domain.repository.RouteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RouteRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RouteRepository {
    
    override suspend fun getActiveRoutes(): Result<List<Route>> {
        return try {
            val routes = apiService.getActiveRoutes().map { it.toDomain() }
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getRouteById(id: String): Result<Route> {
        return try {
            val route = apiService.getRouteById(id).toDomain()
            Result.success(route)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getRoutesByDriver(driverId: String): Result<List<Route>> {
        return try {
            val routes = apiService.getRoutesByDriver(driverId).map { it.toDomain() }
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveRoutesToCache(routes: List<Route>) {
        // TODO: Implementar cache local com Room
    }
    
    override suspend fun getRoutesFromCache(): List<Route> {
        // TODO: Implementar cache local com Room
        return emptyList()
    }
    
    override fun observeActiveRoutes(): Flow<List<Route>> = flow {
        val routes = getActiveRoutes().getOrNull() ?: emptyList()
        emit(routes)
    }

    override suspend fun searchRoutesByText(text: String): Result<List<Route>> {
        return try {
            val routes = apiService.searchRoutesByText(text).map { it.toDomain() }
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun filterRoutes(turno: String?, ativa: Boolean?): Result<List<Route>> {
        return try {
            val response = apiService.filterRoutes(turno, ativa)
            val routes = response.map { it.toDomain() }
            Result.success(routes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createRoute(route: Route): Result<Route> {
        return try {
            val routeDto = route.toDto()
            val response = apiService.createRoute(routeDto)
            val createdRoute = response.toDomain()
            Result.success(createdRoute)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateRoute(route: Route): Result<Route> {
        return try {
            val routeDto = route.toDto()
            val response = apiService.updateRoute(route.id, routeDto)
            val updatedRoute = response.toDomain()
            Result.success(updatedRoute)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteRoute(routeId: String): Result<Unit> {
        return try {
            apiService.deleteRoute(routeId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 