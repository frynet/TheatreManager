package com.frynet.theatre.actors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class ActorService {

    @Autowired
    private lateinit var actorRepository: ActorRepository

    @Autowired
    private lateinit var actorConverter: ActorConverter

    fun getAllActors(): List<ActorInfo> {
        return actorRepository.findAll().map { actorConverter.toInfo(it) }
    }

    fun getActorById(id: Long): ActorInfo {
        val actor = actorRepository.findById(id)

        if (actor.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The actor with id=$id not found")
        }

        return actorConverter.toInfo(actor.get())
    }

    fun addActor(actor: ActorCreate): ActorInfo {
        if (actorRepository.existsByName(actor.name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The actor ${actor.name} already exist")
        }

        return actorConverter.toInfo(
            actorRepository.save(actorConverter.toEntity(actor))
        )
    }

    fun deleteActorById(id: Long) {
        val actor = actorRepository.findById(id)

        if (actor.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The actor with id=$id not found")
        }

        actorRepository.deleteById(id)
    }

    fun deleteAll() {
        actorRepository.deleteAll()
    }
}