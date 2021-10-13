package com.frynet.theatre.spectacles

import org.springframework.stereotype.Service


@Service
class SpectacleConverter {

    fun toInfo(o: SpectacleEntity) = SpectacleInfo(
        o.id,
        o.title
    )

    fun toEntity(o: SpectacleCreate) = SpectacleEntity(
        0L,
        o.title
    )

    fun toCreate(o: SpectacleInfo) = SpectacleCreate(
        o.title
    )
}