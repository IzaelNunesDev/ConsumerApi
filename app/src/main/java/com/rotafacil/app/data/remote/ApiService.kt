package com.rotafacil.app.data.remote

import com.rotafacil.app.data.remote.dto.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.*

interface ApiService {
    
    // Authentication
    @POST("api/v1/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequestDto
    ): LoginResponseDto
    
    @POST("api/v1/auth/register/aluno")
    suspend fun registerStudent(
        @Body studentData: Map<String, String>
    ): LoginResponseDto
    
    // Users
    @GET("api/v1/alunos/{id}")
    suspend fun getStudent(@Path("id") id: String): UserDto
    
    @GET("api/v1/motoristas/{id}")
    suspend fun getDriver(@Path("id") id: String): UserDto
    
    // Routes
    @GET("api/v1/rotas/ativas")
    suspend fun getActiveRoutes(): List<RouteDto>
    
    @GET("api/v1/rotas/{id}")
    suspend fun getRouteById(@Path("id") id: String): RouteDto
    
    @GET("api/v1/rotas/motorista/{motoristaId}")
    suspend fun getRoutesByDriver(@Path("motoristaId") driverId: String): List<RouteDto>
    
    // Trips
    @GET("api/v1/viagens/aluno/{alunoId}")
    suspend fun getTripsByStudent(@Path("alunoId") studentId: String): List<TripDto>
    
    @GET("api/v1/viagens/motorista/{motoristaId}")
    suspend fun getTripsByDriver(
        @Path("motoristaId") driverId: String,
        @Query("data_inicio") startDate: String? = null,
        @Query("data_fim") endDate: String? = null
    ): List<TripDto>
    
    @GET("api/v1/viagens/{id}")
    suspend fun getTripById(@Path("id") id: String): TripDto
    
    @PUT("api/v1/viagens/{id}")
    suspend fun updateTripStatus(
        @Path("id") tripId: String,
        @Body status: Map<String, String>
    ): TripDto
    
    @POST("api/v1/viagens/{id}/incidentes")
    suspend fun reportIncident(
        @Path("id") tripId: String,
        @Body incident: IncidentRequestDto
    ): IncidentDto
    
    @POST("api/v1/frequencias")
    suspend fun markAttendance(@Body attendance: AttendanceRequestDto): AttendanceDto
    
    // Vehicles
    @GET("api/v1/veiculos/{id}")
    suspend fun getVehicle(@Path("id") id: String): VehicleDto
    
    // FCM Token
    @POST("api/v1/alunos/{id}/fcm-token")
    suspend fun updateFcmToken(
        @Path("id") studentId: String,
        @Body token: Map<String, String>
    ): Unit
}

@JsonClass(generateAdapter = true)
data class VehicleDto(
    val id: String,
    val placa: String,
    val modelo: String,
    @Json(name = "capacidade_passageiros")
    val capacidade: Int,
    @Json(name = "ano_fabricacao")
    val ano: Int,
    @Json(name = "status_manutencao")
    val status: String = "Disponível",
    @Json(name = "adaptado_pcd")
    val adaptadoPcd: Boolean = false
) 