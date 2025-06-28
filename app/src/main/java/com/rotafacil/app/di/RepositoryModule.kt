package com.rotafacil.app.di

import com.rotafacil.app.data.repository.AuthRepositoryImpl
import com.rotafacil.app.data.repository.RouteRepositoryImpl
import com.rotafacil.app.data.repository.TripRepositoryImpl
import com.rotafacil.app.data.repository.VehicleRepositoryImpl
import com.rotafacil.app.domain.repository.AuthRepository
import com.rotafacil.app.domain.repository.RouteRepository
import com.rotafacil.app.domain.repository.TripRepository
import com.rotafacil.app.domain.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindRouteRepository(
        routeRepositoryImpl: RouteRepositoryImpl
    ): RouteRepository
    
    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository
    
    @Binds
    @Singleton
    abstract fun bindVehicleRepository(
        vehicleRepositoryImpl: VehicleRepositoryImpl
    ): VehicleRepository
} 