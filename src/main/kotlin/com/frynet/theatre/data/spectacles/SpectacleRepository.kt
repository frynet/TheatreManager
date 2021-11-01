package com.frynet.theatre.data.spectacles

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface SpectacleRepository : CrudRepository<SpectacleEntity, Long> {

    fun existsByTitle(title: String): Boolean
}