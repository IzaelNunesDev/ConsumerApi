package com.rotafacil.app.domain.model

data class Location(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class TripLocation(
    val tripId: String,
    val location: Location,
    val speed: Float? = null,
    val heading: Float? = null
) 