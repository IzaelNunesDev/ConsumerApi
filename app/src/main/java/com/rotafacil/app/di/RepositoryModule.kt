package com.rotafacil.app.di

import com.rotafacil.app.data.repository.AuthRepositoryImpl
import com.rotafacil.app.domain.repository.AuthRepository
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
    
    // TODO: Add other repository bindings when implementations are created
    // @Binds
    // @Singleton
    // abstract fun bindTripRepository(
    //     tripRepositoryImpl: TripRepositoryImpl
    // ): TripRepository
    //
    // @Binds
    // @Singleton
    // abstract fun bindRouteRepository(
    //     routeRepositoryImpl: RouteRepositoryImpl
    // ): RouteRepository
} 