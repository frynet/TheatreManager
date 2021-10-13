package com.frynet.theatre.spectacles

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

    fun getAll(): List<SpectacleInfo> {
        return spectacleRepository.findAll().map { spectacleConverter.toInfo(it) }
    }

    fun getById(id: Long): SpectacleInfo {
        val s = spectacleRepository.findById(id)

        if (s.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The spectacle with id=$id not found")
        }

        return spectacleConverter.toInfo(s.get())
    }

    fun add(spec: SpectacleCreate): SpectacleInfo {
        if (spectacleRepository.existsByTitle(spec.title)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "The spectacle with title ${spec.title} already exists"
            )
        }

        return spectacleConverter.toInfo(
            spectacleRepository.save(spectacleConverter.toEntity(spec))
        )
    }

    fun updateById(id: Long, spec: SpectacleCreate): SpectacleInfo {
        val s = spectacleRepository.findById(id)

        if (s.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The spectacle with id=$id not found")
        }

        s.get().title = spec.title

        return spectacleConverter.toInfo(spectacleRepository.save(s.get()))
    }

    fun deleteById(id: Long) {
        val s = spectacleRepository.findById(id)

        if (s.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "The spectacle with id=$id not found")
        }

        spectacleRepository.delete(s.get())
    }

    fun deleteAll() {
        spectacleRepository.deleteAll()
    }
}