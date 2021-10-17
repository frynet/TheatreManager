package com.frynet.theatre.spectacles

import com.frynet.theatre.errors.Spectacle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class SpectacleService {

    @Autowired
    private lateinit var spectacleRepository: SpectacleRepository

    @Autowired
    private lateinit var spectacleConverter: SpectacleConverter

    private fun getIfExists(id: Long): SpectacleEntity {
        val spectacle = spectacleRepository.findById(id)

        if (spectacle.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Spectacle.notFound(id))
        }

        return spectacle.get()
    }

    fun getAll(): List<SpectacleInfo> {
        return spectacleRepository.findAll().map { spectacleConverter.toInfo(it) }
    }

    fun getById(id: Long): SpectacleInfo {
        return spectacleConverter.toInfo(getIfExists(id))
    }

    fun add(spec: SpectacleCreate): SpectacleInfo {
        if (spectacleRepository.existsByTitle(spec.title)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Spectacle.alreadyExist(spec.title))
        }

        return spectacleConverter.toInfo(
            spectacleRepository.save(spectacleConverter.toEntity(spec))
        )
    }

    fun updateById(id: Long, spec: SpectacleCreate): SpectacleInfo {
        val s = getIfExists(id)

        s.title = spec.title

        return spectacleConverter.toInfo(spectacleRepository.save(s))
    }

    fun deleteById(id: Long) {
        spectacleRepository.delete(getIfExists(id))
    }

    fun deleteAll() {
        spectacleRepository.deleteAll()
    }
}