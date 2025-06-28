fun StopPointDto.toStopPoint(): StopPoint {
    return StopPoint(
        id = id,
        nome = nome,
        ordem = ordem,
        latitude = latitude,
        longitude = longitude,
        horarioEstimado = horarioEstimado
    )
}

fun StopPoint.toStopPointDto(): StopPointDto {
    return StopPointDto(
        id = id,
        nome = nome,
        ordem = ordem,
        latitude = latitude,
        longitude = longitude,
        horarioEstimado = horarioEstimado
    )
} 