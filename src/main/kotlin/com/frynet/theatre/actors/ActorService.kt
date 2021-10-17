package com.frynet.theatre.actors

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

    fun getAllActors(): List<ActorInfo> {
        return actorRepository.findAll().map { actorConverter.toInfo(it) }
    }

    fun getActorById(id: Long): ActorInfo {
        val actor = actorRepository.findById(id)

        if (actor.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Actor.notFound(id))
        }

        return actorConverter.toInfo(actor.get())
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
        val a = actorRepository.findById(id)

        if (a.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Actor.notFound(id))
        }

        a.get().name = actor.name

        return actorConverter.toInfo(actorRepository.save(a.get()))
    }

    fun deleteActorById(id: Long) {
        val actor = actorRepository.findById(id)

        if (actor.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Actor.notFound(id))
        }

        actorRepository.deleteById(id)
    }

    fun deleteAll() {
        actorRepository.deleteAll()
    }
}