package com.frynet.theatre.spectacles_actors

import com.frynet.theatre.actors.ActorService
import com.frynet.theatre.errors.SpectacleActor
import com.frynet.theatre.spectacles.SpectacleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class SpecActorService {

    @Autowired
    private lateinit var specActorRepository: SpecActorRepository

    @Autowired
    private lateinit var actorService: ActorService

    @Autowired
    private lateinit var spectacleService: SpectacleService

    fun getActorsInSpec(specId: Long): List<Long> {
        return specActorRepository.findBySpecId(specId).map { it.id.actor }
    }

    fun addActorsToSpec(specId: Long, actors: List<Long>): List<Long> {
        val nonExistent = mutableListOf<Long>()

        spectacleService.getById(specId)

        actors.forEach {
            try {
                actorService.getActorById(it)

                specActorRepository.save(SpecActorEntity(SpecActorId(specId, it)))
            } catch (ex: ResponseStatusException) {
                nonExistent.add(it)
            }
        }

        return nonExistent
    }

    fun deleteActorFromSpec(specId: Long, actorId: Long) {
        val id = SpecActorId(specId, actorId)

        if (specActorRepository.existsById(id)) {
            specActorRepository.deleteById(id)
        } else {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, SpectacleActor.notFound(id))
        }
    }

    fun deleteAll() {
        specActorRepository.deleteAll()
    }
}