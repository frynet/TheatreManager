package com.frynet.theatre.repertoires

import com.frynet.theatre.errors.Repertoire
import com.frynet.theatre.spectacles.SpectacleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate


@Service
class RepertoireService {

    @Autowired
    private lateinit var repertoireRepository: RepertoireRepository

    @Autowired
    private lateinit var repertoireConverter: RepertoireConverter

    @Autowired
    private lateinit var spectacleService: SpectacleService

    fun getSpectaclesByDate(date: RepertoireDate): List<RepertoireInfo> {
        return repertoireRepository.findByDate(date.date).map { repertoireConverter.toInfo(it) }
    }

    fun getSpectaclesByInterval(interval: RepertoireDateInterval): Map<LocalDate, List<RepertoireInfo>> {
        if (interval.begin.isAfter(interval.end)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Repertoire.invalidDateInterval(interval))
        }

        return repertoireRepository
            .findByInterval(interval.begin, interval.end)
            .map { repertoireConverter.toInfo(it) }
            .groupBy { it.date }
    }

    fun scheduleSpectacle(info: RepertoireInfo) {
        spectacleService.getById(info.specId)

        val id = RepertoireId(info.specId, info.date)

        if (repertoireRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Repertoire.alreadyScheduled(info))
        }

        repertoireRepository.save(RepertoireEntity(id))
    }

    fun cancelSpectacle(info: RepertoireInfo) {
        spectacleService.getById(info.specId)

        val id = RepertoireId(info.specId, info.date)

        if (!repertoireRepository.existsById(id)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, Repertoire.notScheduled(info))
        }

        repertoireRepository.deleteById(id)
    }

    fun deleteAll() {
        repertoireRepository.deleteAll()
    }
}