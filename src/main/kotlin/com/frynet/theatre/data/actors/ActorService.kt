package com.frynet.theatre.data.actors

import com.frynet.theatre.errors.Actor
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

    private fun getIfExists(id: Long): ActorEntity {
        val actor = actorRepository.findById(id)

        if (actor.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Actor.notFound(id))
        }

        return actor.get()
    }

    fun getAllActors(): List<ActorInfo> {
        return actorRepository.findAll().map { actorConverter.toInfo(it) }
    }

    fun getActorById(id: Long): ActorInfo {
        return actorConverter.toInfo(getIfExists(id))
    }

    fun addActor(actor: ActorCreate): ActorInfo {
        if (actorRepository.existsByName(actor.name)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Actor.alreadyExist(actor.name))
        }

        return actorConverter.toInfo(
            actorRepository.save(actorConverter.toEntity(actor))
        )
    }

    fun updateActor(id: Long, actor: ActorCreate): ActorInfo {
        val a = getIfExists(id)

        a.name = actor.name

        return actorConverter.toInfo(actorRepository.save(a))
    }

    fun deleteActorById(id: Long) {
        actorRepository.delete(getIfExists(id))
    }

    fun deleteAll() {
        actorRepository.deleteAll()
    }
}