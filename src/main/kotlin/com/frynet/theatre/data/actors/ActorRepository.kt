package com.frynet.theatre.data.actors

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


@Repository
interface ActorRepository : CrudRepository<ActorEntity, Long> {

    fun existsByName(name: String): Boolean
}